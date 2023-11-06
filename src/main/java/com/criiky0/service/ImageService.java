package com.criiky0.service;

import com.criiky0.pojo.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;

import java.util.HashMap;
import java.util.List;

/**
 * @author 50309
 * @description 针对表【image】的数据库操作Service
 * @createDate 2023-11-01 13:27:34
 */
public interface ImageService extends IService<Image> {

    Result<ResultCodeEnum> deletePhoto(Long imageId, Long userId);

    Result<ResultCodeEnum> uploadMany(List<Image> images, Long userId);

    Result<ResultCodeEnum> deletePhotos(List<Long> list, Long userId);

    Result<HashMap<String, Object>> selectPage(Integer page, Integer size, Long userId);
}
