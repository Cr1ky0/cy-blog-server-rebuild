package com.criiky0.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
    private Long menuId;

    private String title;

    private String icon;

    private String color;

    private Integer level;

    private Integer sort;

    private Long belongMenuId;

    private Long userId;

    private List<MenuDTO> subMenu;
}
