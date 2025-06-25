package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.dto.request.AuthRequest;
import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.impl.AuthServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/auth")
@RestController
public class AuthController {
    @Autowired
    AuthServiceImpl authService;

    //用邮箱登录
    @PostMapping({"/email"})
    public LocalToken loginByEmail(@RequestBody AuthRequest request) throws BusinessException{
        String email = request.getEmail();
        String password = request.getPassword();

        System.out.println("get a LoginByPhoneNumber request: " + "email = " + email + "; password = " + password); // 添加日志
        try {
            LocalToken response = authService.loginByEmail(email, password);
            log.info("生成 token: {}", response.getToken());
            return response;
        }catch (BusinessException be){
            log.warn("LoginByPhoneNumber request status: " + be.getMessage());
            log.error("生成 token 时出现异常: ", be);
            throw be;
        }

    }

    //用电话号码登录
    @PostMapping({"/phoneNumber"})
    public LocalToken loginByPhoneNumber(@RequestBody AuthRequest request) throws BusinessException{
        String phoneNumber = request.getPhoneNumber();
        String password = request.getPassword();
        log.info("get a LoginByPhoneNumber request: " + "phoneNumber = "
 + phoneNumber + "; password = " + password);
        try {
            LocalToken response = authService.loginByPhoneNumber(phoneNumber, password);
            log.info("LoginByPhoneNumber request status: success");
            return response;
        }catch (BusinessException be){
            log.warn("LoginByPhoneNumber request status: " + be.getMessage());
            throw be;
        }

    }

}
