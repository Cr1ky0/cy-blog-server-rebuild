package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName blog
 */
@TableName(value ="blog")
@Data
public class Blog implements Serializable {
    @TableId
    private Long blogId;

    private String title;

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

    private Long menuId;

    private static final long serialVersionUID = 1L;
}