package com.criiky0.service;

import com.criiky0.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.criiky0.utils.Result;

import java.util.HashMap;

/**
* @author criiky0
* @description 针对表【comment】的数据库操作Service
* @createDate 2023-10-26 14:24:13
*/
public interface CommentService extends IService<Comment> {

    Result<HashMap<String,Comment>> addComment(Comment comment);
}
