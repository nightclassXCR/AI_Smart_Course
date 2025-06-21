package com.dd.ai_smart_course.exception;

import com.dd.ai_smart_course.exception.errorcode.ErrorCode;

//基础异常类
public class BusinessException extends RuntimeException{
    private final int code;  // 错误码

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        // 使用枚举错误码
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
