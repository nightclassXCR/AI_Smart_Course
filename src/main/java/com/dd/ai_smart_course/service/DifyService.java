package com.dd.ai_smart_course.service;

public interface DifyService {

    /**
     * 向Dify发送问题并获取答案
     * @param question 用户问题
     * @param userId 用户ID
     * @param courseId 课程ID（可选）
     * @return Dify返回的答案
     */
    String askQuestion(String question, Long userId, Long courseId);
    
    /**
     * 获取Dify服务状态
     * @return 服务是否可用
     */
    boolean isServiceAvailable();
    
    /**
     * 配置Dify API密钥
     * @param apiKey API密钥
     */
    void configureApiKey(String apiKey);
}
