package com.dd.ai_smart_course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    //用@Bean注解的方法会返回一个对象，这个对象会被 Spring 管理
    @Bean
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.163.com");
        sender.setPort(587);
        sender.setUsername("wu1825270596@163.com"); //graygoo个人网易邮箱
        sender.setPassword("HDpdFQcvLMesNzgG");     //graygoo个人网易邮箱的smtp服务密钥

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.verify-code.expire-time",300);  //设置邮件最长时延
        props.put("mail.verify-code.length",6);         //设置验证码长度
        return sender;
    }
}

