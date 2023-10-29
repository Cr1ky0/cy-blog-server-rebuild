package com.criiky0.pojo.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterVO {
    @Length(min=3,max=20)
    private String username;
    @Length(min=6)
    private String password;
    @Length(min=3,max=20)
    private String nickname;
    @NotBlank
    private String email;
    @NotNull
    private Integer code;
}
