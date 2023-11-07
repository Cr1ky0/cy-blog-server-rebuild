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
 * @TableName menu
 */
@TableName(value ="menu")
@Data
public class Menu implements Serializable {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    @Length(min=1,max=30,message = "title过长或过短！")
    @NotBlank(message = "菜单标题不能为空")
    private String title;

    @NotBlank(message = "icon不能为空")
    private String icon;

    @NotBlank(message = "color不能为空")
    private String color;

    private Integer level;

    private Integer sort;

    @Version
    private Integer version;

    private Integer deleted;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long belongMenuId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    private static final long serialVersionUID = 1L;
}