package com.criiky0.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class UpdateBlogVO {
    @NotNull(message = "博客id不能为空！")
    private Long blogId;

    @Length(min=1,max = 30,message = "博客标题过长或过短")
    private String title;

    @Length(min=1,message = "博客长度过短！")
    private String content;

    private Boolean collected;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    private Long menuId;
}
