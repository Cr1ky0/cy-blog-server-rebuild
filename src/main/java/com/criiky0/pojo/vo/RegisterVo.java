package com.criiky0.pojo.vo;

import lombok.Data;

@Data
public class RegisterVo {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private Integer code;
}
