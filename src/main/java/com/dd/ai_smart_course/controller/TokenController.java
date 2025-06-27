package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
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
    public Result<Boolean> checkToken(@RequestBody LocalToken localToken){
        log.info("get a checkToken request");
        return Result.success(tokenImpl.checkToken(localToken));
    }

    @PostMapping("/checkAdmin")
    public Result<Boolean> checkAdmin(@RequestBody LocalToken localToken){
        log.info("get a checkAdmin request");
        return Result.success(tokenImpl.checkAdmin(localToken));
    }

    @PostMapping("/checkTeacher")
    public Result<Boolean> checkTeacher(@RequestBody LocalToken localToken){
        log.info("get a checkAdmin request");
        return Result.success(tokenImpl.checkTeacher(localToken));
    }

    @PostMapping("/checkNormal")
    public Result<Boolean> checkNormal(@RequestBody LocalToken localToken){
        log.info("get a checkNormal request");
        return Result.success(tokenImpl.checkNormal(localToken));
    }
}
