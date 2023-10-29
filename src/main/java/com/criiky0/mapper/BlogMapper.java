package com.criiky0.mapper;

import com.criiky0.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author criiky0
* @description 针对表【blog】的数据库操作Mapper
* @createDate 2023-10-26 14:24:12
* @Entity com.criiky0.pojo.Blog
*/
public interface BlogMapper extends BaseMapper<Blog> {

    Integer findMaxSort(Long menuId);
}




