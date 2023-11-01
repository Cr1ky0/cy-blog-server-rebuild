package com.criiky0.service;

import com.criiky0.pojo.OssConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
* @author 50309
* @description 针对表【oss_config】的数据库操作Service
* @createDate 2023-10-30 17:20:42
*/
public interface OssConfigService extends IService<OssConfig> {

    Result<HashMap<String,OssConfig>> addConfig(OssConfig ossConfig);

    Result<Map<String,String>> getPolicy();
}
