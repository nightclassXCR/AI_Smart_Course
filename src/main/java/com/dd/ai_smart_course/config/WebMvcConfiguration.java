package com.dd.ai_smart_course.config;


import com.dd.ai_smart_course.interceptor.JwtAuthenticationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器并配置拦截路径
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/**") // 拦截所有的请求
                .excludePathPatterns(
                        "/auth/email",  // 登录接口放行
                        "/auth/phoneNumber",
                        "/auth/register", // 注册接口放行
                        "/swagger-ui/**",   // Swagger放行
                        "/v3/api-docs/**"   // OpenAPI文档放行
                );
    }
}