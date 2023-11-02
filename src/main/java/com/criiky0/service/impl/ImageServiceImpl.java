package com.criiky0.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.VoidResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.mapper.OssConfigMapper;
import com.criiky0.pojo.Image;
import com.criiky0.pojo.OssConfig;
import com.criiky0.service.ImageService;
import com.criiky0.mapper.ImageMapper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 50309
 * @description 针对表【image】的数据库操作Service实现
 * @createDate 2023-11-01 13:27:34
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    private ImageMapper imageMapper;

    private OssConfigMapper ossConfigMapper;

    @Autowired
    public ImageServiceImpl(ImageMapper imageMapper, OssConfigMapper ossConfigMapper) {
        this.imageMapper = imageMapper;
        this.ossConfigMapper = ossConfigMapper;
    }

    @Override
    public Result<ResultCodeEnum> deletePhoto(Long imageId, Long userId) {
        Image image = imageMapper.selectById(imageId);
        if (!Objects.equals(image.getUserId(), userId)) {
            return Result.build(null, ResultCodeEnum.NOT_COUNTERPART);
        }
        OssConfig config = ossConfigMapper.selectOne(new QueryWrapper<>());
        // key
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory
            .newDefaultCredentialProvider(config.getAccessKeyId(), config.getAccessKeySecret());
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(config.getEndpoint(), credentialsProvider);
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            VoidResult result = ossClient.deleteObject(image.getBucket(), image.getFileName());
            ResponseMessage response = result.getResponse();
            if (response.getStatusCode() == 204) {
                int deleted = imageMapper.deleteById(image);
                if (deleted > 0) {
                    return Result.ok(null);
                }
            }
            return Result.build(null, 400, "删除失败，请重新再试");
        } catch (Exception oe) {
            return Result.build(null, 400, "删除失败，请重新再试！");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public Result<ResultCodeEnum> uploadMany(List<Image> images, Long userId) {
        OssConfig config = ossConfigMapper.selectOne(new QueryWrapper<>());
        if (config == null) {
            return Result.build(null, ResultCodeEnum.OSS_CONFIG_NOT_EXIST);
        }
        String bucket = config.getBucket();
        String endpoint = config.getEndpoint();
        for (Image image : images) {
            image.setEndpoint(endpoint);
            image.setBucket(bucket);
            image.setUserId(userId);
            imageMapper.insert(image);
        }
        return Result.ok(null);
    }

    @Override
    public Result<ResultCodeEnum> deletePhotos(List<Long> list, Long userId) {
        OssConfig config = ossConfigMapper.selectOne(new QueryWrapper<>());
        // key
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory
            .newDefaultCredentialProvider(config.getAccessKeyId(), config.getAccessKeySecret());
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(config.getEndpoint(), credentialsProvider);
        try {
            for (Long id : list) {
                Image image = imageMapper.selectById(id);
                if (image != null) {
                    // 删除文件或目录。如果要删除目录，目录必须为空。
                    VoidResult result = ossClient.deleteObject(image.getBucket(), image.getFileName());
                    ResponseMessage response = result.getResponse();
                    if (response.getStatusCode() == 204) {
                        int deleted = imageMapper.deleteById(image);
                        if (deleted > 0) {
                            continue;
                        }
                    }
                    return Result.build(null, 400, "部分删除失败，请重新再试");
                }
            }
        } catch (Exception oe) {
            return Result.build(null, 400, "部分删除失败，请重新再试！");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return Result.ok(null);
    }
}
