package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.criiky0.pojo.OssConfig;
import com.criiky0.service.OssConfigService;
import com.criiky0.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/oss")
@CrossOrigin
public class OSSController {
    private OssConfigService ossConfigService;

    @Autowired
    public OSSController(OssConfigService ossConfigService) {
        this.ossConfigService = ossConfigService;
    }

    /**
     * 添加config
     * @param ossConfig
     * @return
     */
    @PostMapping
    public Result<HashMap<String,OssConfig>> addConfig(@Validated @RequestBody OssConfig ossConfig){
        return ossConfigService.addConfig(ossConfig);
    }

    /**
     * 获取config
     * @return
     */
    @GetMapping
    public Result<HashMap<String,OssConfig>> getConfig(){
        OssConfig config = ossConfigService.getOne(new QueryWrapper<>());
        HashMap<String, OssConfig> map = new HashMap<>();
        map.put("config",config);
        return Result.ok(map);
    }
}
