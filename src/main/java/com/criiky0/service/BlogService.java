package com.criiky0.service;

import com.criiky0.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.criiky0.pojo.dto.BlogDTO;
import com.criiky0.pojo.vo.UpdateBlogVO;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;

import java.util.HashMap;
import java.util.List;

/**
* @author criiky0
* @description 针对表【blog】的数据库操作Service
* @createDate 2023-10-26 14:24:12
*/
public interface BlogService extends IService<Blog> {

    Result<HashMap<String,Blog>> addBlog(Blog blog);

    Result<ResultCodeEnum> deleteBlog(Long blogId, Long userId);

    Result<HashMap<String,Blog>> updateBlog(UpdateBlogVO updateBlogVO, Long userId);

    Result<ResultCodeEnum> deleteBlogsOfMenu(Long menuId, Long userId);

    Result<HashMap<String,Object>> getBlogPageOfCriiky0(Integer page, Integer size,Boolean collected);

    Result<HashMap<String, List<BlogDTO>>> getCollectedListOfCriiky0();

    Result<HashMap<String, List<BlogDTO>>> getTimeLineOfCriiky0();
}
