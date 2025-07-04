package com.dd.ai_smart_course.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dify")
public class DifyController {
    @RequestMapping("/test")
    public String test() {
        return "Dify Service is working";
    }
}
