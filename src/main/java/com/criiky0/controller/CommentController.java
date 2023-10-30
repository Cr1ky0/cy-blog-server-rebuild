package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.criiky0.pojo.Comment;
import com.criiky0.service.CommentService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 添加评论
     * 
     * @param comment
     * @param result
     * @return
     */
    @PostMapping("/post")
    public Result<HashMap<String, Comment>> addComment(@Validated @RequestBody Comment comment, BindingResult result) {
        if (result.hasErrors()) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
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
        boolean removed = commentService.removeById(commentId);
        if (removed)
            return Result.ok(null);
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * 更新评论浏览数据
     * @param commentId
     * @param plus
     * @return
     */
    @PatchMapping("/browse")
    public Result<HashMap<String,Comment>> updateCommentLikes(@RequestParam("comment_id") Long commentId,
        @RequestParam(value = "plus", defaultValue = "true") boolean plus) {
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        int num = comment.getLikes() + 1;
        if (!plus) {
            if(comment.getLikes() == 0)
                num = 0;
            else
                num = comment.getLikes() - 1;
        }
        boolean updated = commentService.update(
            new LambdaUpdateWrapper<Comment>().eq(Comment::getCommentId, commentId).set(Comment::getLikes, num));
        if(updated){
            comment.setLikes(num);
            HashMap<String, Comment> map = new HashMap<>();
            map.put("updatedComment",comment);
            return Result.ok(map);
        }
        return Result.build(null,ResultCodeEnum.UNKNOWN_ERROR);
    }
}
