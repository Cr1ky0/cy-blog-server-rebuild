package com.criiky0.pojo.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterVO {
    @Length(min=3,max=20,message = "用户名!")
    @NotBlank(message = "请填写用户名！")
    private String username;
    @Length(min=6,message = "密码不得小于6位！")
    @NotBlank(message = "请填写密码！")
    private String password;
    @Length(min=3,max=20,message = "昵称过长或过短！")
    @NotBlank
    private String nickname;
    @NotBlank(message = "请填写邮箱！")
    private String email;
    @NotNull(message = "验证码不能为空！")
    private Integer code;
}
