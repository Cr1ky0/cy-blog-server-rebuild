package com.criiky0.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    private Long menuId;

    private String title;

    private String icon;

    private String color;

    private Integer level;

    private Integer sort;

    private Long belongMenuId;

    private Long userId;

    private List<BlogDTO> blogs;

    private List<MenuDTO> subMenu;
}
