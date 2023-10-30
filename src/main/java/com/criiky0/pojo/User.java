package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    @TableId
    private Long userId;

    @Length(min=3,max=20)
    @NotBlank
    private String username;

    @Length(min=3,max=20)
    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    @Length(max=50)
    private String brief;

    @NotBlank
    private String email;

    private String avatar;

    private Object role;

    private Date createAt;

    @Version
    private Integer version;

    private Integer deleted;

    private static final long serialVersionUID = 1L;
}