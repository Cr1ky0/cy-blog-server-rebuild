package com.criiky0.pojo.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateMenuVO {
    @NotNull
    private Long menuId;
    @Length(min=1,max = 30)
    private String title;
    private String icon;
    private String color;
    private Long belongMenuId;
}
