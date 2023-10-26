package com.criiky0.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Blog;
import com.criiky0.service.BlogService;
import com.criiky0.mapper.BlogMapper;
import org.springframework.stereotype.Service;

/**
* @author criiky0
* @description 针对表【blog】的数据库操作Service实现
* @createDate 2023-10-26 14:24:12
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService{

}




