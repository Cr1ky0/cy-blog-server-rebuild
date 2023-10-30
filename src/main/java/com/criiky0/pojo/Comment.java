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

    @NotBlank
    private String content;

    @Min(0)
    private Integer likes;

    @Length(max = 20)
    private String username;

    @Length(max = 50)
    private String brief;

    private Date createAt;

    @Version
    private Integer version;

    private Integer deleted;

    private Long belongCommentId;

    private Long userId;

    @NotNull
    private Long blogId;

    private static final long serialVersionUID = 1L;
}