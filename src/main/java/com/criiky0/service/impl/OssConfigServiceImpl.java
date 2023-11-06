package com.criiky0.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.OssConfig;
import com.criiky0.service.OssConfigService;
import com.criiky0.mapper.OssConfigMapper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 50309
 * @description 针对表【oss_config】的数据库操作Service实现
 * @createDate 2023-10-30 17:20:42
 */
@Service
@Transactional
public class OssConfigServiceImpl extends ServiceImpl<OssConfigMapper, OssConfig> implements OssConfigService {

    private OssConfigMapper ossConfigMapper;

    @Autowired
    public OssConfigServiceImpl(OssConfigMapper ossConfigMapper) {
        this.ossConfigMapper = ossConfigMapper;
    }

    @Override
    public Result<HashMap<String, OssConfig>> addConfig(OssConfig ossConfig) {
        QueryWrapper<OssConfig> queryWrapper = new QueryWrapper<>();
        OssConfig config = ossConfigMapper.selectOne(queryWrapper);
        // 先删除旧Config
        if (config != null) {
            int delete = ossConfigMapper.deleteById(config.getId());
            if (delete == 0) {
                return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
            }
        }
        // 添加Config
        int insert = ossConfigMapper.insert(ossConfig);
        if (insert > 0) {
            HashMap<String, OssConfig> map = new HashMap<>();
            map.put("config", ossConfig);
            return Result.ok(map);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    @Override
    public Result<Map<String,String>> getPolicy() {
        OssConfig config = ossConfigMapper.selectOne(new QueryWrapper<>());
        if (config == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        // key
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory
            .newDefaultCredentialProvider(config.getAccessKeyId(), config.getAccessKeySecret());
        // host
        String host = "https://" + config.getBucket() + "." + config.getEndpoint();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(config.getEndpoint(), credentialsProvider);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为500MB，即CONTENT_LENGTH_RANGE为50*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 524288000);
            // dir
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, config.getDir());

            // policy
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String accessId = credentialsProvider.getCredentials().getAccessKeyId();
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", config.getDir());
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime));

            // 生成回调
            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", config.getCallbackUrl());
            jasonCallback.put("callbackBody",
                "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);

            return Result.ok(respMap);

        } catch (Exception e) {
            return Result.build(null, 400, e.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }
}
