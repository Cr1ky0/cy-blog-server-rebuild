package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.criiky0.pojo.Blog;
import com.criiky0.pojo.Comment;
import com.criiky0.pojo.dto.CommentDTO;
import com.criiky0.service.BlogService;
import com.criiky0.service.CommentService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private CommentService commentService;

    private BlogService blogService;

    private final String SYS_PATH = System.getProperty("user.dir");

    @Autowired
    public CommentController(CommentService commentService, BlogService blogService) {
        this.commentService = commentService;
        this.blogService = blogService;
    }

    /**
     * 添加评论
     *
     * @param comment
     * @return
     */
    @PostMapping("/post")
    public Result<HashMap<String, Comment>> addComment(@Validated @RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @DeleteMapping
    public Result<ResultCodeEnum> deleteComment(@RequestParam("comment_id") Long commentId) {
        return commentService.deleteComment(commentId);
    }

    /**
     * 更新评论浏览数据
     *
     * @param commentId
     * @param plus
     * @return
     */
    @PatchMapping("/browse")
    public Result<HashMap<String, Comment>> updateCommentLikes(@RequestParam("comment_id") Long commentId,
        @RequestParam(value = "plus", defaultValue = "true") boolean plus) {
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        int num = comment.getLikes() + 1;
        if (!plus) {
            if (comment.getLikes() == 0)
                num = 0;
            else
                num = comment.getLikes() - 1;
        }
        boolean updated = commentService.update(
            new LambdaUpdateWrapper<Comment>().eq(Comment::getCommentId, commentId).set(Comment::getLikes, num));
        if (updated) {
            comment.setLikes(num);
            HashMap<String, Comment> map = new HashMap<>();
            map.put("updatedComment", comment);
            return Result.ok(map);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * 获取当前blog评论分页
     * 
     * @param blogId
     * @return
     */
    @GetMapping("/curblog")
    public Result<HashMap<String, Object>> getAllCommentOfBlog(@RequestParam("blog_id") Long blogId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "5") Integer size,
        @RequestParam(value = "sort", defaultValue = "creat_at") String sort,
        @RequestParam(value = "options", defaultValue = "") String options) {
        boolean exists = blogService.exists(new LambdaQueryWrapper<Blog>().eq(Blog::getBlogId, blogId));
        if (!exists) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        return commentService.getCommentPageOfBlog(blogId, page, size, sort, options);
    }

    /**
     * 删除该博客下所有comment
     * 
     * @param blogId
     * @return
     */
    @DeleteMapping("/blog")
    public Result<ResultCodeEnum> deleteCommentsOfBlog(@RequestParam("blog_id") Long blogId) {
        boolean exists = blogService.exists(new LambdaQueryWrapper<Blog>().eq(Blog::getBlogId, blogId));
        if (!exists) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        return commentService.deleteAllOfBlog(blogId);
    }

    /**
     * 获取单个Comment
     * 
     * @param commentId
     * @return
     */
    @GetMapping("/single/{id}")
    public Result<HashMap<String, Comment>> getSingle(@PathVariable("id") Long commentId) {
        Comment comment = commentService.getById(commentId);
        if (comment == null)
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        HashMap<String, Comment> map = new HashMap<>();
        map.put("comment", comment);
        return Result.ok(map);
    }

    /**
     * 获取博客评论计数
     *
     * @param blogId
     * @return
     */
    @GetMapping("/curblog/count")
    public Result<HashMap<String, Long>> getCountOfBlog(@RequestParam("blog_id") Long blogId) {
        Blog blog = blogService.getById(blogId);
        if (blog == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        long count = commentService.count(new LambdaQueryWrapper<Comment>().eq(Comment::getBlogId, blogId));
        HashMap<String, Long> map = new HashMap<>();
        map.put("count", count);
        return Result.ok(map);
    }

    /**
     * 获取emoji
     * 
     * @return
     */
    @GetMapping("/emoji")
    public Result<HashMap<String, Map>> getEmojis() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(SYS_PATH + "/public/emoji", "emoji.json");
        // 从JSON文件转换为Map
        Map<String, Object> jsonMap = objectMapper.readValue(file, new TypeReference<>() {});
        HashMap<String, Map> resultMap = new HashMap<>();
        resultMap.put("emojis", jsonMap);
        return Result.ok(resultMap);
    }
}
