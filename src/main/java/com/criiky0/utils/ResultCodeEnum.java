package com.criiky0.utils;

/**
 * 统一返回结果状态信息类
 *
 */
public enum ResultCodeEnum {

    SUCCESS(200, "success"),

    // user
    USERINFO_ERROR(400, "用户名或密码错误！"), CODE_ERROR(400, "验证码错误！"), REGISTER_ERROR(400, "账号或密码不合规！"),
    USER_USED_ERROR(400, "用户名已被占用！"), NOT_LOGIN(401, "未登录，无法操作！"), EMAIL_USED(400, "Email已被占用！"),
    ROLE_NOT_ALLOW(401, "权限不足！"), EMAIL_NOT_CORRECT(400, "不是对应的邮箱，无法注册！"), AVATAR_TO_LARGE(400, "图片过大，请重新上传！"),
    // universal
    OPERATION_ERROR(400, "非法操作！"), UNKNOWN_ERROR(400, "未知错误！"), PARAM_NULL_ERROR(400, "参数为空！"),
    OCCUR_EXCEPTION(500, "参数非法或服务器内部异常，请重试或联系管理！"), CANNOT_FIND_ERROR(400, "找不到对应实体！"),
    PARAM_ERROR(400, "提供的参数过长/短或参数过大/小或参数为空或参数不符合规范，请检查后重新提交！");

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