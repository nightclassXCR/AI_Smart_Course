package com.dd.ai_smart_course.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class SimpleCodeManager  {

    //静态邮件发送状态字符串
    private static final String STATUS_EMAIL_ADDRESS_ERROR = "EMAIL_ADDRESS_ERROR";
    private static final String STATUS_SENDING_SUCCESS = "SENDING_SUCCESS";
    private static final String STATUS_SENDING_FAIL = "SENDING_FAIL";

    //静态的邮件发件人


    private MailSender mailSender;
    private SimpleMailMessage templateMessage;

    @Autowired
    public SimpleCodeManager(MailSender mailSender) {
        this.mailSender = mailSender;

        // 初始化模板邮件
        this.templateMessage = new SimpleMailMessage();
        templateMessage.setFrom("your-email@example.com");  // 发件人
        templateMessage.setSubject("验证码");  // 主题
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    //发送验证码
    public String sendIdentifyingCode(String userEmail, String identifyingCode) {
        //验证邮箱地址是否规范
        if(!isValidEmail(userEmail)){
            return STATUS_EMAIL_ADDRESS_ERROR;
        }

        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(userEmail);
        msg.setText("Dear " + "your identifying code is" + identifyingCode);
        try {
            this.mailSender.send(msg);
        }
        catch (Exception e) {
            //发送失败
            log.error("验证码发送失败: ",e);
            return STATUS_SENDING_FAIL;
        }
        return STATUS_SENDING_SUCCESS;
    }

    //使用正则表达式验证邮箱格式
    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}