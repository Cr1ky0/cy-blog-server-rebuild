package com.criiky0.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Comment;
import com.criiky0.service.CommentService;
import com.criiky0.mapper.CommentMapper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
* @author criiky0
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2023-10-26 14:24:13
*/
@Service
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    private CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public Result<HashMap<String,Comment>> addComment(Comment comment) {
        int insert = commentMapper.insert(comment);
        if(insert > 0){
            Comment newComment = commentMapper.selectById(comment.getCommentId());
            HashMap<String, Comment> map = new HashMap<>();
            map.put("comment",newComment);
            return Result.ok(map);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }
}




