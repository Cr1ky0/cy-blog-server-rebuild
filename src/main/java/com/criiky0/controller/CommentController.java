package com.criiky0.controller;

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

    @PostMapping
    public Result<HashMap<String,Comment>> addComment(@Validated @RequestBody Comment comment, BindingResult result){
        if(result.hasErrors()){
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        return commentService.addComment(comment);
    }
}
