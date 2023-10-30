package com.criiky0.mapper;

import com.criiky0.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.criiky0.pojo.dto.CommentDTO;

import java.util.List;

/**
* @author criiky0
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2023-10-26 14:24:13
* @Entity com.criiky0.pojo.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentDTO> selectTopCommentDTOs(Long blogId);

    List<CommentDTO> selectSubComment(Long commentId);
}




