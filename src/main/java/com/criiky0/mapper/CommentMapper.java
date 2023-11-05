package com.criiky0.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.criiky0.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.criiky0.pojo.dto.CommentDTO;

import java.util.List;
import java.util.Map;

/**
 * @author criiky0
 * @description 针对表【comment】的数据库操作Mapper
 * @createDate 2023-10-26 14:24:13
 * @Entity com.criiky0.pojo.Comment
 */
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<Map> selectCommentDTOsOfBlog(IPage<CommentDTO> page, Long blogId);

    List<CommentDTO> selectSubComment(Long commentId);
}
