package com.dd.ai_smart_course.config;


import com.dd.ai_smart_course.interceptor.JwtAuthenticationInterceptor;
import com.dd.ai_smart_course.utils.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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

    /**
     * 扩展Spring MVC框架的消息转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        converter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,converter);
    }


}