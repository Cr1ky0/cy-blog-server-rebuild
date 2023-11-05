package com.criiky0.service;

import com.criiky0.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.criiky0.pojo.dto.CommentDTO;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;

import java.util.HashMap;
import java.util.List;

/**
 * @author criiky0
 * @description 针对表【comment】的数据库操作Service
 * @createDate 2023-10-26 14:24:13
 */
public interface CommentService extends IService<Comment> {

    Result<HashMap<String, Comment>> addComment(Comment comment);

    List<CommentDTO> findSubComment(CommentDTO rootComment);

    Result<HashMap<String,Object>> getCommentPageOfBlog(Long blogId, Integer page, Integer size);

    Result<ResultCodeEnum> deleteAllOfBlog(Long blogId);
}
