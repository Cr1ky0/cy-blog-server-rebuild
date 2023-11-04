package com.criiky0.pojo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BlogDTO {
    private Long blogId;

    private String title;

    private Integer sort;

    private Date createAt;
}
