package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @TableName blog
 */
@TableName(value ="blog")
@Data
public class Blog implements Serializable {
    @TableId
    private Long blogId;

    @Length(min=1,max = 30)
    private String title;

    @NotBlank
    private String content;

    private Integer likes;

    private Integer views;

    private Date createAt;

    private Date updateAt;

    private Integer sort;

    @Version
    private Integer version;

    private Integer deleted;

    private Long userId;

    @NotNull
    private Long menuId;

    private static final long serialVersionUID = 1L;
}