package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.criiky0.pojo.Image;
import com.criiky0.pojo.OssConfig;
import com.criiky0.service.ImageService;
import com.criiky0.service.OssConfigService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/image")
@Validated
public class ImageController {

    private ImageService imageService;

    private OssConfigService ossConfigService;

    @Autowired
    public ImageController(ImageService imageService, OssConfigService ossConfigService) {
        this.imageService = imageService;
        this.ossConfigService = ossConfigService;
    }

    /**
     * 上传单张photo
     * @param image
     * @param userId
     * @return
     */
    @PostMapping
    public Result<HashMap<String, Image>> addPhoto(@Valid @RequestBody Image image,
        @RequestAttribute("userid") Long userId) {
        OssConfig config = ossConfigService.getOne(new QueryWrapper<>());
        if (config == null) {
            return Result.build(null, ResultCodeEnum.OSS_CONFIG_NOT_EXIST);
        }
        image.setUserId(userId);
        image.setBucket(config.getBucket());
        image.setEndpoint(config.getEndpoint());
        boolean saved = imageService.save(image);
        if (saved) {
            HashMap<String, Image> map = new HashMap<>();
            map.put("image", image);
            return Result.ok(map);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * 批量上传
     * @param images
     * @param userId
     * @return
     */
    @PostMapping("/many")
    public Result<ResultCodeEnum> addPhotos(@Valid @RequestBody List<Image> images,
        @RequestAttribute("userid") Long userId) {
        return imageService.uploadMany(images, userId);
    }

    /**
     * 删除photo
     * @param imageId
     * @return
     */
    @DeleteMapping
    public Result<ResultCodeEnum> deletePhoto(@RequestParam("image_id") Long imageId,
        @RequestAttribute("userid") Long userId) {
        Image image = imageService.getById(imageId);
        if (image == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        return imageService.deletePhoto(imageId,userId);
    }

    /**
     * 批量删除
     * @param idList
     * @param userId
     * @return
     */
    @DeleteMapping("/many")
    public Result<ResultCodeEnum> deletePhotos(@RequestBody List<Long> idList,@RequestAttribute("userid") Long userId) {
        if (idList.isEmpty()) {
            return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
        }
        return imageService.deletePhotos(idList,userId);
    }
}
