package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.criiky0.pojo.Image;
import com.criiky0.pojo.OssConfig;
import com.criiky0.service.ImageService;
import com.criiky0.service.OssConfigService;
import com.criiky0.utils.OSSUtils;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oss")
public class OSSController {
    private OssConfigService ossConfigService;

    private ImageService imageService;

    @Autowired
    public OSSController(OssConfigService ossConfigService, ImageService imageService) {
        this.ossConfigService = ossConfigService;
        this.imageService = imageService;
    }

    /**
     * 添加config
     *
     * @param ossConfig
     * @return
     */
    @PostMapping
    public Result<HashMap<String, OssConfig>> addConfig(@Validated @RequestBody OssConfig ossConfig) {
        return ossConfigService.addConfig(ossConfig);
    }

    /**
     * 获取config
     * 
     * @return
     */
    @GetMapping
    public Result<HashMap<String, OssConfig>> getConfig() {
        OssConfig config = ossConfigService.getOne(new QueryWrapper<>());
        if (config == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        HashMap<String, OssConfig> map = new HashMap<>();
        map.put("config", config);
        return Result.ok(map);
    }

    /**
     * 获取Policy
     * 
     * @return
     */
    @GetMapping("/policy")
    public Result<Map<String, String>> getPolicy() {
        return ossConfigService.getPolicy();
    }

    /**
     * 接收OSS发送的POST请求（上传回调） 需要上线后的外网链接才能访问回调，开发阶段无法使用
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/callback")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ossCallbackBody =
            OSSUtils.GetPostBody(request.getInputStream(), Integer.parseInt(request.getHeader("content-length")));
        boolean ret = OSSUtils.VerifyOSSCallbackRequest(request, ossCallbackBody);
        System.out.println("verify result : " + ret);
        System.out.println("OSS Callback Body:" + ossCallbackBody);
        if (ret) {
            // TODO:图片其他信息上传
            // 获取filename
            String filename = request.getParameter("filename");
            // 获取oss信息
            OssConfig config = ossConfigService.getOne(new QueryWrapper<>());
            Image image = new Image();
            image.setFileName(filename);
            image.setBucket(config.getBucket());
            image.setEndpoint(config.getEndpoint());
            // 添加到数据库
            boolean save = imageService.save(image);
            if (save)
                OSSUtils.response(request, response, "{\"Status\":\"OK\"}", HttpServletResponse.SC_OK);
            else
                OSSUtils.response(request, response, "{\"Status\":\"server upload failed!\"}", HttpServletResponse.SC_BAD_REQUEST);
        } else {
            OSSUtils.response(request, response, "{\"Status\":\"verify not ok\"}", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
