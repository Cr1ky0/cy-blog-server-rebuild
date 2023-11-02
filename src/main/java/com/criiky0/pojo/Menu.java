package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

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

    private Long belongMenuId;

    private Long userId;

    private static final long serialVersionUID = 1L;
}