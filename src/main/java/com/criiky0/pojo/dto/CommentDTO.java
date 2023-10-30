package com.criiky0.pojo.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentDTO {
    private Long commentId;

    private String content;

    private Integer likes;

    private String username;

    private String brief;

    private Date createAt;

    private Long belongCommentId;

    private Long userId;

    private Long blogId;

    private List<CommentDTO> subComments;
}
