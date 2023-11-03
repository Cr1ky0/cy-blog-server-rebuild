package com.criiky0.pojo.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginVo {
    private String userinfo;

    @NotBlank(message = "请填写密码！")
    private String password;

    @NotBlank(message = "请填写验证码！")
    private String verificationCode;
}
