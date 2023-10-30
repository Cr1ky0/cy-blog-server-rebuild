package com.criiky0.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.OssConfig;
import com.criiky0.service.OssConfigService;
import com.criiky0.mapper.OssConfigMapper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
* @author 50309
* @description 针对表【oss_config】的数据库操作Service实现
* @createDate 2023-10-30 17:20:42
*/
@Service
public class OssConfigServiceImpl extends ServiceImpl<OssConfigMapper, OssConfig>
    implements OssConfigService{

    private OssConfigMapper ossConfigMapper;

    @Autowired
    public OssConfigServiceImpl(OssConfigMapper ossConfigMapper) {
        this.ossConfigMapper = ossConfigMapper;
    }

    @Override
    public Result<HashMap<String,OssConfig>> addConfig(OssConfig ossConfig) {
        QueryWrapper<OssConfig> queryWrapper = new QueryWrapper<>();
        OssConfig config = ossConfigMapper.selectOne(queryWrapper);
        // 先删除旧Config
        if(config != null){
            int delete = ossConfigMapper.deleteById(config.getId());
            if(delete == 0){
                return Result.build(null,ResultCodeEnum.UNKNOWN_ERROR);
            }
        }
        // 添加Config
        int insert = ossConfigMapper.insert(ossConfig);
        if(insert > 0){
            HashMap<String, OssConfig> map = new HashMap<>();
            map.put("OSSConfig",ossConfig);
            return Result.ok(map);
        }
        return Result.build(null,ResultCodeEnum.UNKNOWN_ERROR);
    }
}




