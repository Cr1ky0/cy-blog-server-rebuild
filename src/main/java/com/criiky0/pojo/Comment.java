package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @TableName comment
 */
@TableName(value = "comment")
@Data
public class Comment implements Serializable {
    @TableId
    private Long commentId;

    @NotBlank(message = "评论不能为空！")
    private String content;

    @Min(value = 0,message = "likes最小值为0！")
    private Integer likes;

    @Length(max = 20,message = "username过长！")
    private String username;

    @Length(max = 50,message = "个人简介过长！")
    private String brief;

    private Date createAt;

    @Version
    private Integer version;

    private Integer deleted;

    private Long belongCommentId;

    private Long userId;

    @NotNull(message = "blogId不能为空")
    private Long blogId;

    private static final long serialVersionUID = 1L;
}