package com.dd.ai_smart_course;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@MapperScan("com.dd.ai_smart_course.mapper")
public class AiSmartCourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiSmartCourseApplication.class, args);
    }

}
