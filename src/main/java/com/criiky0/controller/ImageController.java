package com.criiky0.controller;

import com.criiky0.pojo.Image;
import com.criiky0.service.ImageService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * 添加照片
     * @param image
     * @param result
     * @param userId
     * @return
     */
    @PostMapping
    public Result<HashMap<String, Image>> addPhoto(@Validated @RequestBody Image image, BindingResult result,
        @RequestAttribute("userid") Long userId) {
        if (result.hasErrors()) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        image.setUserId(userId);
        boolean saved = imageService.save(image);
        if(saved){
            HashMap<String, Image> map = new HashMap<>();
            map.put("image",image);
            return Result.ok(map);
        }
        return Result.build(null,ResultCodeEnum.UNKNOWN_ERROR);
    }
}
