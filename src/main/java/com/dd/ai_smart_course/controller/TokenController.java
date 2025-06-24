package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.service.impl.TokenImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@Slf4j
public class TokenController {
    @Autowired
    TokenImpl tokenImpl;

    @PostMapping("/normalCheck")
    public boolean checkToken(LocalToken localToken){
        return tokenImpl.checkToken(localToken);
    }
}
