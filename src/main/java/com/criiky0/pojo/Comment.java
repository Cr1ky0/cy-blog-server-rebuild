package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    @TableId
    private Long commentId;

    private String content;

    private Integer likes;

    private String username;

    private String brief;

    private Date createAt;

    @Version
    private Integer version;

    private Integer deleted;

    private Long belongCommentId;

    private Long userId;

    private Long blogId;

    private static final long serialVersionUID = 1L;
}