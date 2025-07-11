package com.dd.ai_smart_course.service.base;

public interface VerificationCodeService {
    // 发送验证码邮件
    void sendVerificationCode(String email);
    // 验证验证码的方法
    boolean verifyCode(String email, String userInputCode);
}
