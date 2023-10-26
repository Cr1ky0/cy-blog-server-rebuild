package com.criiky0.utils;

/**
 * 统一返回结果状态信息类
 *
 */
public enum ResultCodeEnum {

    SUCCESS(200, "success"),
    USERINFO_ERROR(400,"用户名或密码错误！"),
    REGISTER_ERROR(400, "账号或密码不合规！"),

    OPERATION_ERROR(400,"非法操作！"),
    NOT_LOGIN(401, "未登录，无法操作！");


    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}