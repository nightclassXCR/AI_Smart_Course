package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.service.DifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DifyServiceImpl implements DifyService {

    @Value("${dify.api.key:}")
    private String apiKey;
    
    @Value("${dify.api.url:https://api.dify.ai/v1/chat-messages}")
    private String apiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String askQuestion(String question, Long userId, Long courseId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", new HashMap<>());
            requestBody.put("query", question);
            requestBody.put("response_mode", "blocking");
            requestBody.put("user", "user_" + userId);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body.containsKey("answer")) {
                    return (String) body.get("answer");
                }
            }
            
            return "抱歉，暂时无法获取答案，请稍后重试。";
        } catch (Exception e) {
            log.error("调用Dify API失败", e);
            return "服务暂时不可用，请稍后重试。";
        }
    }

    @Override
    public boolean isServiceAvailable() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    @Override
    public void configureApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
