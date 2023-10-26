package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName menu
 */
@TableName(value ="menu")
@Data
public class Menu implements Serializable {
    @TableId
    private Long menuId;

    private String title;

    private String icon;

    private String color;

    private Integer level;

    private Integer sort;

    @Version
    private Integer version;

    private Integer active;

    private Long belongMenuId;

    private Long userId;

    private static final long serialVersionUID = 1L;
}