package com.criiky0.pojo.vo;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class UpdateBlogVO {
    @NotNull
    private Long blogId;

    @Length(min=1,max = 30)
    private String title;

    @Length(min=1)
    private String content;

    @Future
    private Date updateAt;

    private Long menuId;
}
