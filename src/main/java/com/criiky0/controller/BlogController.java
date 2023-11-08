package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.criiky0.pojo.Blog;
import com.criiky0.pojo.User;
import com.criiky0.pojo.dto.BlogDTO;
import com.criiky0.pojo.vo.UpdateBlogVO;
import com.criiky0.service.BlogService;
import com.criiky0.service.UserService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    private BlogService blogService;

    private UserService userService;

    @Autowired
    public BlogController(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
    }

    /**
     * 添加博客（同时添加至ES索引）
     * @param blog
     * @param userId
     * @return
     */
    @PostMapping
    public Result<HashMap<String, Blog>> addBlog(@Validated @RequestBody Blog blog,
        @RequestAttribute("userid") Long userId) {
        blog.setUserId(userId);
        return blogService.addBlog(blog);
    }

    /**
     * 根据id获取博客
     * 
     * @param menuId
     * @return
     */
    @GetMapping("/single/{id}")
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
     * 
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
     * 
     * @param updateBlogVO
     * @param userId
     * @return
     */
    @PatchMapping
    public Result<HashMap<String, Blog>> updateBlog(@Validated @RequestBody UpdateBlogVO updateBlogVO,
        @RequestAttribute("userid") Long userId) {
        // 检验参数
        List<Object> paramList = Arrays.asList(updateBlogVO.getTitle(), updateBlogVO.getContent(),
            updateBlogVO.getMenuId(), updateBlogVO.getUpdateAt(), updateBlogVO.getCollected());
        boolean isAllNull = paramList.stream().allMatch(Objects::isNull);
        if (isAllNull) {
            return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
        }
        return blogService.updateBlog(updateBlogVO, userId);
    }

    /**
     * 删除指定menu下的所有blogs
     * 
     * @param menuId
     * @param userId
     * @return
     */
    @DeleteMapping("/menu")
    public Result<ResultCodeEnum> deleteBlogsOfMenu(@RequestParam("menu_id") Long menuId,
        @RequestAttribute("userid") Long userId) {
        return blogService.deleteBlogsOfMenu(menuId, userId);
    }

    /**
     * 获取博客分页数据 这里获取的是我自己的博客
     * 
     * @param page
     * @param size
     * @param options 自定义options，用,隔开每个查询条件（&options=collected:true,sort:likes）
     * @return
     */
    @GetMapping("/criiky0")
    public Result<HashMap<String, Object>> getBlogPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size,
        @RequestParam(value = "sort", defaultValue = "create_at") String sort,
        @RequestParam(value = "options", defaultValue = "") String options) {
        if (page <= 0 || size <= 0) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        User criiky0 = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "criiky0"));
        return blogService.getBlogPageOfUser(page, size, sort, options, criiky0.getUserId());
    }

    /**
     * 更新浏览数据
     * 
     * @param blogId
     * @param like
     * @param plus
     * @return
     */
    @PatchMapping("/browse")
    public Result<HashMap<String, Blog>> updateBlogBrowse(@RequestParam("blog_id") Long blogId,
        @RequestParam(value = "like", defaultValue = "true") boolean like,
        @RequestParam(value = "plus", defaultValue = "false") boolean plus) {
        Blog blog = blogService.getById(blogId);
        if (blog == null)
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        // 更新likes
        if (like) {
            int num = blog.getLikes() + 1;
            if (!plus) {
                if (blog.getLikes() == 0)
                    num = 0;
                else
                    num = blog.getLikes() - 1;
            }
            boolean updated = blogService
                .update(new LambdaUpdateWrapper<Blog>().eq(Blog::getBlogId, blogId).set(Blog::getLikes, num));
            if (updated) {
                blog.setLikes(num);
                HashMap<String, Blog> map = new HashMap<>();
                map.put("updatedBlog", blog);
                return Result.ok(map);
            }
        }
        // 更新views
        else {
            int num = blog.getViews() + 1;
            if (!plus) {
                if (blog.getViews() == 0)
                    num = 0;
                else
                    num = blog.getViews() - 1;
            }
            boolean updated = blogService
                .update(new LambdaUpdateWrapper<Blog>().eq(Blog::getBlogId, blogId).set(Blog::getViews, num));
            if (updated) {
                blog.setViews(num);
                HashMap<String, Blog> map = new HashMap<>();
                map.put("updatedBlog", blog);
                return Result.ok(map);
            }
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * 查询criiky0的收藏列表
     * 
     * @return
     */
    @GetMapping("/criiky0/collected")
    public Result<HashMap<String, List<BlogDTO>>> getCollectedBlogOfCriiky0() {
        return blogService.getCollectedListOfCriiky0();
    }

    /**
     * 查询criiky0的TimeLine
     * 
     * @return
     */
    @GetMapping("/criiky0/timeline")
    public Result<HashMap<String, List<BlogDTO>>> getTimeLineOfCriiky0() {
        return blogService.getTimeLineOfCriiky0();
    }

    /**
     * 获取指定menu下的blogs
     * 
     * @param menuId
     * @return
     */
    @GetMapping("/certain_menu")
    public Result<HashMap<String, List<BlogDTO>>> getBlogsOfMenu(@RequestParam("menu_id") Long menuId) {
        return blogService.getBlogDTOOfMenu(menuId);
    }

    /**
     * 修改排序
     * 
     * @param idList
     * @return
     */
    @PatchMapping("/sort")
    public Result<ResultCodeEnum> sort(@RequestBody List<Long> idList, @RequestAttribute("userid") Long userId) {
        return blogService.sort(idList, userId);
    }

    /**
     * 获取criiky0的blog数量
     * 
     * @return
     */
    @GetMapping("/criiky0/count")
    public Result<HashMap<String, Long>>
        getCountOfCriiky0(@RequestParam(value = "options", defaultValue = "") String options) {
        User criiky0 = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "criiky0"));
        long count = blogService.countByUserWithOptions(criiky0.getUserId(),options);
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", count);
        return Result.ok(map);
    }

    /**
     * 查询有comment的blog
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/hascomment")
    public Result<HashMap<String, Object>> getBlogHasCommentOfUser(@RequestAttribute("userid") Long userId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return blogService.getBlogHasCommentOfUser(page, size, userId);
    }
}
