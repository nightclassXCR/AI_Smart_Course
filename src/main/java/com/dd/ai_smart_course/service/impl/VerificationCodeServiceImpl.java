package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.service.base.VerificationCodeService;
import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private final RedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    // 从配置文件读取发件人地址
    @Value("${spring.mail.username}")
    private String fromEmail;

    // 邮箱验证码前缀
    private final String EMAIL_CODE_PREFIX = "email_code" + "_";

    // 验证码有效期（分钟）
    private final long EXPIRE_MINUTES = 5;
    // 验证码长度
    private final int CODE_LENGTH = 6;

    /**
     * 发送验证码邮件
     */
    @Override
    public void sendVerificationCode(String email) throws BusinessException{
        String code = generateRandomCode();
        saveCodeToRedis(email, code);
        sendEmail(email, code);
    }

    /**
     * 验证用户输入的验证码
     */
    @Override
    public boolean verifyCode(String email, String userInputCode) {
        String key = getRedisKey(email);
        String storedCode = (String) redisTemplate.opsForValue().get(key);

        boolean isValid = userInputCode.equals(storedCode);
        if (isValid) {
            redisTemplate.delete(key); // 验证通过后删除缓存
        }
        return isValid;
    }

    // 生成随机验证码
    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    // 保存验证码到Redis
    private void saveCodeToRedis(String email, String code) throws BusinessException{
        String key = getRedisKey(email);
        try {
            redisTemplate.opsForValue().set(key, code, EXPIRE_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e){
            log.error("Redis异常", e);
            throw new BusinessException(ErrorCode.MAIL_REDIS_BREAK);
        }
    }

    // 发送邮件
    private void sendEmail(String email, String code) throws BusinessException{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail); // 显式设置发件人
        message.setTo(email);
        message.setSubject("【系统】验证码");
        message.setText("您的验证码是：" + code + "\n有效期" + EXPIRE_MINUTES + "分钟，请不要泄露给他人。");
        try{
            mailSender.send(message);
        } catch (Exception e){
            log.error("邮件发送失败: ",e);
            throw new BusinessException(ErrorCode.MAIL_SEND_FAILS);
        }
    }

    // 构建Redis键
    private String getRedisKey(String email) {
        return EMAIL_CODE_PREFIX + email;
    }
}