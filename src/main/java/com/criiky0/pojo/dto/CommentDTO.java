package com.criiky0.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentId;

    private String content;

    private Integer likes;

    private String username;

    private String brief;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long belongCommentId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long blogId;

    private List<CommentDTO> subComments;
}
