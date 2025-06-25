package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.LocalToken;
import com.dd.ai_smart_course.service.impl.TokenImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
@Slf4j
public class TokenController {
    @Autowired
    TokenImpl tokenImpl;

    @PostMapping("/checkToken")
    public boolean checkToken(@RequestBody LocalToken localToken){
        log.info("get a checkToken request");
        return tokenImpl.checkToken(localToken);
    }

    @PostMapping("/checkAdmin")
    public boolean checkAdmin(@RequestBody LocalToken localToken){
        log.info("get a checkAdmin request");
        return  tokenImpl.checkAdmin(localToken);
    }

    @PostMapping("/checkNormal")
    public boolean checkNormal(@RequestBody LocalToken localToken){
        log.info("get a checkNormal request");
        return  tokenImpl.checkNormal(localToken);
    }
}
