package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @Length(min=3,max=20,message = "用户名!")
    @NotBlank(message = "请填写用户名！")
    private String username;

    @Length(min=3,max=20,message = "昵称过长或过短！")
    @NotBlank
    private String nickname;

    @NotBlank(message = "请填写密码！")
    private String password;

    @Length(max=50,message = "个人简介过长！")
    private String brief;

    @NotBlank(message = "请填写邮箱！")
    private String email;

    private String avatar;

    private Object role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @Version
    private Integer version;

    private Integer deleted;

    private static final long serialVersionUID = 1L;
}