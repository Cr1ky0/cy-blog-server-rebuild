package com.criiky0.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Image;
import com.criiky0.service.ImageService;
import com.criiky0.mapper.ImageMapper;
import org.springframework.stereotype.Service;

/**
* @author 50309
* @description 针对表【image】的数据库操作Service实现
* @createDate 2023-11-01 13:27:34
*/
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
    implements ImageService{

}




