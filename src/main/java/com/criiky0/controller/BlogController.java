package com.criiky0.controller;

import com.criiky0.pojo.Blog;
import com.criiky0.pojo.vo.UpdateBlogVO;
import com.criiky0.service.BlogService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin
public class BlogController {

    private BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    /**
     * 添加博客（同时添加至ES索引）
     * 
     * @param blog
     * @param result
     * @param userId
     * @return
     */
    @PostMapping
    public Result<HashMap<String, Blog>> addBlog(@Validated @RequestBody Blog blog, BindingResult result,
        @RequestAttribute("userid") Long userId) {
        if (result.hasErrors()) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        blog.setUserId(userId);
        return blogService.addBlog(blog);
    }

    /**
     * 根据id获取博客
     * 
     * @param menuId
     * @return
     */
    @GetMapping("/{id}")
    public Result<HashMap<String, Blog>> getBlog(@PathVariable("id") Long menuId) {
        Blog blog = blogService.getById(menuId);
        if (blog == null)
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        HashMap<String, Blog> map = new HashMap<>();
        map.put("blog", blog);
        return Result.ok(map);
    }

    /**
     * 删除博客
     * @param blogId
     * @param userId
     * @return
     */
    @DeleteMapping
    public Result<ResultCodeEnum> delBlog(@RequestParam("blog_id") Long blogId,
        @RequestAttribute("userid") Long userId) {
        return blogService.deleteBlog(blogId, userId);
    }

    /**
     * 更新博客
     * @param updateBlogVO
     * @param result
     * @param userId
     * @return
     */
    @PatchMapping
    public Result<HashMap<String,Blog>> updateBlog(@Validated @RequestBody UpdateBlogVO updateBlogVO, BindingResult result,
        @RequestAttribute("userid") Long userId) {
        if (result.hasErrors()) {
            return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
        }
        // 检验参数
        List<Object> paramList = Arrays.asList(updateBlogVO.getTitle(), updateBlogVO.getContent(), updateBlogVO.getLikes(),
            updateBlogVO.getViews(), updateBlogVO.getMenuId());
        boolean isAllNull = paramList.stream().allMatch(Objects::isNull);
        if(isAllNull){
            return Result.build(null,ResultCodeEnum.PARAM_NULL_ERROR);
        }
        return blogService.updateBlog(updateBlogVO,userId);
    }
}
