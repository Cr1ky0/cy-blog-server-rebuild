package com.criiky0.pojo.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMenuVO {
    @NotNull
    private Long menuId;
    private String title;
    private String icon;
    private String color;
    private Long belongMenuId;
}
