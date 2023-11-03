package com.criiky0.pojo.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdatePswVo {
    @NotBlank
    private String oldPsw;
    @NotBlank
    @Length(min = 6, message = "新密码长度不能小于6位")
    private String newPsw;
    @NotBlank
    private String pswConfirm;
}
