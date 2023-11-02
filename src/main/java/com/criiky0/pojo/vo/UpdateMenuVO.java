package com.criiky0.pojo.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateMenuVO {
    @NotNull(message = "menuId不能为空！")
    private Long menuId;
    @Length(min=1,max = 30,message = "title过长或过短！")
    private String title;
    private String icon;
    private String color;
    private Long belongMenuId;
}
