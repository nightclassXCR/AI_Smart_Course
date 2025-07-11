package com.dd.ai_smart_course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:*", "http://www.localhost:*","http://82.157.104.71:*","http://172.26.208.1:*") // 包含5173端口
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 确保包含OPTIONS
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 预检请求缓存时间
    }
}