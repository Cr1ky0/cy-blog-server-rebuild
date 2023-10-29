package com.criiky0.pojo.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterVO {
    @Length(min=3,max=20)
    private String username;
    private String password;
    @Length(min=3,max=20)
    private String nickname;
    private String email;
    private Integer code;
}
