package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.criiky0.pojo.Image;
import com.criiky0.pojo.OssConfig;
import com.criiky0.pojo.User;
import com.criiky0.service.ImageService;
import com.criiky0.service.OssConfigService;
import com.criiky0.service.UserService;
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

    private UserService userService;
    private OssConfigService ossConfigService;

    @Autowired
    public ImageController(ImageService imageService, UserService userService, OssConfigService ossConfigService) {
        this.imageService = imageService;
        this.userService = userService;
        this.ossConfigService = ossConfigService;
    }

    /**
     * 上传单张photo
     * 
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
     * 
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
     * 
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
        return imageService.deletePhoto(imageId, userId);
    }

    /**
     * 批量删除
     * 
     * @param idList
     * @param userId
     * @return
     */
    @DeleteMapping("/many")
    public Result<ResultCodeEnum> deletePhotos(@RequestBody List<Long> idList,
        @RequestAttribute("userid") Long userId) {
        if (idList.isEmpty()) {
            return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
        }
        return imageService.deletePhotos(idList, userId);
    }

    /**
     * 获取criiky0相片分页
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/criiky0")
    public Result<HashMap<String, Object>> getImagesOfCriiky0(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "5") Integer size) {
        User criiky0 = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "criiky0"));
        return imageService.selectPage(page, size, criiky0.getUserId());
    }

    /**
     * 获取当前用户照片分页
     * @param page
     * @param size
     * @param userId
     * @return
     */
    @GetMapping
    public Result<HashMap<String, Object>> getImagePageOfUser(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "5") Integer size, @RequestAttribute("userid") Long userId) {
        User user = userService.getById(userId);
        return imageService.selectPage(page, size, user.getUserId());
    }
}
