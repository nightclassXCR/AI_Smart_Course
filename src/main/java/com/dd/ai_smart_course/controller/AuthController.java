package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.dto.request.AuthRequest;
import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.entity.User;
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
    public Result<LocalToken> loginByEmail(@RequestBody AuthRequest request){
        String email = request.getEmail();
        String password = request.getPassword();

        System.out.println("get a LoginByPhoneNumber request: " + "email = " + email + "; password = " + password); // 添加日志
        try {
            LocalToken response = authService.loginByEmail(email, password);
            log.info("LoginByPhoneNumber request status: success:{}",response);
            return Result.success(response);
        }catch (BusinessException be){
            log.warn("LoginByPhoneNumber request status: " + be.getMessage());
            return Result.error(be.getCode(), be.getMessage());
        }

    }

    //用电话号码登录
    @PostMapping({"/phoneNumber"})
    public Result<LocalToken> loginByPhoneNumber(@RequestBody AuthRequest request) throws BusinessException{
        String phoneNumber = request.getPhoneNumber();
        String password = request.getPassword();

        log.info("get a LoginByPhoneNumber request: " + "phoneNumber = " + phoneNumber + "; password = " + password);
        try {
            LocalToken response = authService.loginByPhoneNumber(phoneNumber, password);
            log.info("LoginByPhoneNumber request status: success111");
            return Result.success(response);
        }catch (BusinessException be){
            log.warn("LoginByPhoneNumber request status: " + be.getMessage());
            return Result.error(be.getCode(), be.getMessage());
        }

    }

    //注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        //稍后改
        try {
            log.info("get a register request");
            boolean result = authService.register(user);
            if(result){
                return Result.success(result);
            }
            return Result.error("something happening in mapper");
        }catch (BusinessException be){
            return Result.error(be.getCode(), be.getMessage());
        }
    }
}
