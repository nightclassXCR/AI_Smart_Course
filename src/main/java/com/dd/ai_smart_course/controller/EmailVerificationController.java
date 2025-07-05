package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.impl.VerificationCodeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/verificationCode")
public class EmailVerificationController {

    @Autowired
    private final VerificationCodeServiceImpl verificationService;

    public EmailVerificationController(VerificationCodeServiceImpl verificationService) {
        this.verificationService = verificationService;
    }

    /**
     * 发送验证码
     */
    @PostMapping("/email/sendCode")
    public Result<Boolean> sendVerificationCode(@RequestParam String email) {
        log.info("get a request: send verification code by email {}", email);
        try {
            verificationService.sendVerificationCode(email);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 验证验证码
     */
    @PostMapping("/email/verifyCode")
    public Result<Boolean> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        log.info("get a request: verify code by email {}", email);

        try {
            boolean isValid = verificationService.verifyCode(email, code);
            return Result.success(isValid);
        } catch (Exception e){
            return Result.error("验证码验证失败");
        }
    }
}