package com.criiky0.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    private String title;

    private String icon;

    private String color;

    private Integer level;

    private Integer sort;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long belongMenuId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    private List<BlogDTO> blogs;

    private List<MenuDTO> subMenu;
}
