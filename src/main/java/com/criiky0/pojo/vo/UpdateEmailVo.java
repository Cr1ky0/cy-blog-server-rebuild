package com.criiky0.pojo.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEmailVo {
    @NotBlank
    private String newEmail;
    @NotNull
    private Integer code;
}
