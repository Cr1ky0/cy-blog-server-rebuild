package com.criiky0.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class BlogDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long blogId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    private String title;

    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
}
