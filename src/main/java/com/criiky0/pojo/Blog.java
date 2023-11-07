package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JsonSerialize(using = ToStringSerializer.class)
    private Long blogId;

    @Length(min=1,max = 30,message = "博客标题过长或过短！")
    @NotBlank(message = "博客标题不能为空")
    private String title;

    @Length(min=1,message = "博客长度过短！")
    @NotBlank(message = "博客内容不能为空！")
    private String content;

    private Integer likes;

    private Integer views;

    private Boolean collected;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    private Integer sort;

    @Version
    private Integer version;

    private Integer deleted;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @NotNull(message = "所属菜单ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    private static final long serialVersionUID = 1L;
}