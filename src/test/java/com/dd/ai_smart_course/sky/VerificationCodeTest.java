package com.dd.ai_smart_course.sky;

import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.impl.VerificationCodeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VerificationCodeTest {

    @Autowired
    private VerificationCodeServiceImpl verificationService;

    // 测试目的地邮箱g
    private static final String TEST_EMAIL = "1825270596@qq.com";

    // 测试发送验证码
    @Test
    public void testSendVerificationCode() {
        try {
            verificationService.sendVerificationCode(TEST_EMAIL);
            System.out.println("验证码已发送至您的邮箱");
        } catch (BusinessException e){
            System.out.println("发送验证码失败：" + e.getMessage());
        }
    }

    // 测试验证码验证
    @Test
    public void testVerifyCode() {
        try {
            String userInputCode = "985353"; // 假设用户输入的验证码为123456
            boolean isValid = verificationService.verifyCode(TEST_EMAIL, userInputCode);
            if (isValid) {
                System.out.println("验证码验证成功");
            } else {
                System.out.println("验证码验证失败");
            }
        } catch (BusinessException e) {
            System.out.println("验证码验证失败：" + e.getMessage());
        }
    }
}
