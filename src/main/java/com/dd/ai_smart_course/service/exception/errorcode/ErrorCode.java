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
    // 用户模块
    EMAIL_EXISTS(4001, "邮箱已被注册"),
    PHONE_EXISTS(4002, "手机号已被注册"),
    USERNAME_EXISTS(4003, "用户名已存在"),

    EMAIL_NULL(5001, "邮箱为空"),
    PHONE_NULL(5002, "手机号为空"),
    USERNAME_NULL(5003, "用户名为空"),
    ROLE_NULL(5004, "用户角色为空"),
    USER_ID_NULL(5005, "用户ID为空"),

    ROLE_ERROR(5051,"用户角色参数错误"),

    USER_NOT_EXISTS(5091, "用户不存在"),
    PASSWORD_WRONG(5092, "密码错误"),

    // QA模块
    QA_ID_NULL(6001, "QA记录ID为空"),
    QA_QUESTION_TEXT_NULL(6002, "QA问题内容为空"),
    QA_NOT_EXISTS(6003, "QA记录不存在"),

    // Log模块
    LOG_NOT_EXISTS(7001, "日志记录不存在"),
    LOG_TARGET_ID_NULL(7002, "日志目标ID为空"),
    LOG_TARGET_TYPE_NULL(7003, "日志目标类型为空"),
    LOG_ACTION_TYPE_NULL(7004, "日志操作类型为空"),
    LOG_TARGET_TYPE_INVALID(7005, "日志目标类型参数错误"),
    LOG_ACTION_TYPE_INVALID(7006, "日志操作类型参数错误");



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
