package com.dd.ai_smart_course.service;


import org.springframework.http.HttpHeaders;
import com.dd.ai_smart_course.dto.DifyCompletionResponse;
import com.dd.ai_smart_course.dto.KnowledgeGraphResponse;
import com.dd.ai_smart_course.dto.request.DifyWorkflowRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
public class DifyService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    @Value("${dify.api.key}")
    private String apiKey;
    @Value("${dify.api.url}")
    private String apiWorkflowUrl;

    public DifyService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper =  objectMapper;
    }

    /**
     * 调用Dify API获取知识图谱
     * Mono 是 Reactor 的一种数据处理模型，用于处理异步数据流。Flux 是 Reactor 的一种数据处理模型，用于处理同步数据流。
     * Mono 和 Flux 都是 Reactor 的核心数据处理模型，用于处理异步数据流。
     * Mono 是一个单值数据流，Flux 是一个多值数据流。
     * Mono 和 Flux 都提供了丰富的操作符，用于处理数据流中的数据。
     * 优势 ： 反应式性，异步处理，可扩展性，可组合性，错误处理，性能优化，测试ability，可维护性，可扩展性，可组合性
     *
     * @param courseName 课程
     * @return KnowledgeGraphResponse 对象
     */

    public Mono<DifyCompletionResponse> callDifyWorkflow(String courseName) {
        Map<String, Object> inputs = Collections.singletonMap("course", courseName); // 确保这里是 "course"

        String userId = UUID.randomUUID().toString();

        DifyWorkflowRequest requestBody = new DifyWorkflowRequest(
                inputs,
                "blocking",
                userId
        );

        return webClient.post()
                .uri(apiWorkflowUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(DifyCompletionResponse.class);
    }
    /**
     * 辅助方法：从Dify的原始响应中提取并解析KnowledgeGraphResponse
     * 这是在 Controller 层处理 DifyCompletionResponse 后调用。
     * @param difyResponse DifyCompletionResponse 对象
     * @return 解析后的 KnowledgeGraphResponse，如果无法解析则返回null或抛出异常
     */
    public KnowledgeGraphResponse extractKnowledgeGraphResponse(DifyCompletionResponse difyResponse) {
        if (difyResponse != null && difyResponse.getData() != null && difyResponse.getData().getOutputs() != null) {
            String rawText = difyResponse.getData().getOutputs().getText();

            // 尝试从字符串中提取JSON部分，去除<think>标签及```json```包裹
            String jsonString = extractJsonFromThoughtText(rawText);

            if (jsonString != null && !jsonString.trim().isEmpty()) {
                try {
                    return objectMapper.readValue(jsonString, KnowledgeGraphResponse.class);
                } catch (Exception e) {
                    System.err.println("Failed to parse KnowledgeGraphResponse from Dify output text: " + e.getMessage());
                    e.printStackTrace();
                    return null; // 或者抛出自定义异常
                }
            }
        }
        return null;
    }

    // 辅助方法：从包含<think>标签和```json```的字符串中提取纯JSON
    private String extractJsonFromThoughtText(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return null;
        }

        // 移除 <think>...</think> 部分
        String cleanedText = rawText.replaceAll("(?s)<think>.*?</think>", "").trim();

        // 查找并提取 ```json 和 ``` 之间的内容
        int jsonStartIndex = cleanedText.indexOf("```json");
        if (jsonStartIndex != -1) {
            jsonStartIndex += "```json".length(); // 跳过 ```json
            int jsonEndIndex = cleanedText.indexOf("```", jsonStartIndex);
            if (jsonEndIndex != -1) {
                return cleanedText.substring(jsonStartIndex, jsonEndIndex).trim();
            }
        }
        // 如果没有找到 ```json 或 ``` 标记，则尝试直接返回清理后的文本，假定它就是JSON
        // 但这可能导致后续的JSON解析失败，如果内容不是纯JSON
        return cleanedText;
    }
}




