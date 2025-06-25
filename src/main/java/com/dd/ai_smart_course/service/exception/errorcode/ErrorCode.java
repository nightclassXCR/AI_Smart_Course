package com.dd.ai_smart_course.service.exception.errorcode;

public enum ErrorCode {
    // SUCCESS、PARAM_ERROR等是ErrorCode枚举类的实例，其类型即为ErrorCode本身
    // 必须枚举在开头
    // 通用错误
    SUCCESS(200, "操作成功"),
    FAILURE(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),

    // 业务错误
    // 查询用户
    EMAIL_EXISTS(4001, "邮箱已被注册"),
    PHONE_EXISTS(4002, "手机号已被注册"),
    USERNAME_EXISTS(4003, "用户名已存在"),

    EMAIL_NULL(5001, "邮箱为空"),
    PHONE_NULL(5002, "手机号为空"),
    USERNAME_NULL(5003, "用户名为空"),

    // 登录模块
    USER_NOT_EXISTS(6001, "用户不存在"),
    PASSWORD_WRONG(6002, "密码错误");



    private final int code;     // 错误码
    private final String message;   // 错误信息

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
