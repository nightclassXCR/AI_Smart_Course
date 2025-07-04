package com.dd.ai_smart_course.dataset;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Dify知识图谱生成服务
 * 负责调用Dify API生成知识图谱JSON数据
 */
@Service
public class DifyKnowledgeGraphService {

    // Dify配置
    @Value("${dify.api.base-url}")
    private String difyApiBaseUrl;

    @Value("${dify.api.key}")
    private String difyApiKey;



    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DifyKnowledgeGraphService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 生成知识图谱JSON数据
     */
    public String generateKnowledgeGraphJson(String query, String userId) throws IOException {
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);
        requestBody.put("user", userId != null ? userId : "default-user");
        requestBody.put("response_mode", "blocking"); // 阻塞模式，等待完整响应
        requestBody.put("conversation_id", ""); // 新对话

        // 添加额外的知识图谱生成指令
        String enhancedQuery = buildKnowledgeGraphPrompt(query);
        requestBody.put("query", enhancedQuery);

        String jsonBody = objectMapper.writeValueAsString(requestBody);
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(difyApiBaseUrl + "/chat-messages")
                .addHeader("Authorization", "Bearer " + difyApiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                // 解析Dify响应，提取知识图谱JSON
                return extractKnowledgeGraphFromResponse(responseBody);
            } else {
                String errorBody = response.body() != null ? response.body().string() : "无错误信息";
                System.err.println("Dify API调用失败: " + response.code() + " " + response.message());
                System.err.println("错误信息: " + errorBody);

                // 返回空的知识图谱
                return getEmptyKnowledgeGraph();
            }
        }
    }

    /**
     * 构建知识图谱生成提示词
     */
    private String buildKnowledgeGraphPrompt(String originalQuery) {
        return String.format("""
            请根据以下查询生成知识图谱：%s
            
            要求：
            1. 从知识库中检索相关的课程、章节、概念信息
            2. 分析实体间的关系（包含、依赖、关联等）
            3. 生成ECharts关系图所需的JSON格式
            4. 只返回JSON数据，不要其他文本
            
            JSON格式要求：
            {
                "nodes": [
                    {
                        "id": "实体ID",
                        "name": "实体名称",
                        "category": "实体类型",
                        "symbolSize": 大小值,
                        "itemStyle": {"color": "颜色"},
                        "label": {"show": true}
                    }
                ],
                "links": [
                    {
                        "source": "源实体ID",
                        "target": "目标实体ID",
                        "name": "关系名称",
                        "lineStyle": {"color": "颜色"}
                    }
                ],
                "categories": [
                    {"name": "课程", "itemStyle": {"color": "#FF6B6B"}},
                    {"name": "章节", "itemStyle": {"color": "#4ECDC4"}},
                    {"name": "概念", "itemStyle": {"color": "#45B7D1"}},
                    {"name": "关系", "itemStyle": {"color": "#96CEB4"}}
                ]
            }
            
            请开始生成：
            """, originalQuery);
    }

    /**
     * 从Dify响应中提取知识图谱JSON
     */
    private String extractKnowledgeGraphFromResponse(String responseBody) {
        try {
            JsonNode responseJson = objectMapper.readTree(responseBody);

            // 从Dify响应中提取答案内容
            String answer = "";
            if (responseJson.has("answer")) {
                answer = responseJson.get("answer").asText();
            } else if (responseJson.has("data") && responseJson.get("data").has("answer")) {
                answer = responseJson.get("data").get("answer").asText();
            }

            // 尝试从答案中提取JSON部分
            String jsonStr = extractJsonFromText(answer);

            // 验证JSON格式
            if (isValidKnowledgeGraphJson(jsonStr)) {
                return jsonStr;
            } else {
                System.err.println("生成的JSON格式不正确，返回空知识图谱");
                return getEmptyKnowledgeGraph();
            }

        } catch (Exception e) {
            System.err.println("解析Dify响应时发生异常: " + e.getMessage());
            return getEmptyKnowledgeGraph();
        }
    }

    /**
     * 从文本中提取JSON字符串
     */
    private String extractJsonFromText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return getEmptyKnowledgeGraph();
        }

        // 查找JSON开始和结束位置
        int jsonStart = text.indexOf("{");
        int jsonEnd = text.lastIndexOf("}");

        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
            String jsonStr = text.substring(jsonStart, jsonEnd + 1);

            // 清理可能的markdown代码块标记
            jsonStr = jsonStr.replaceAll("```json", "").replaceAll("```", "").trim();

            return jsonStr;
        }

        return getEmptyKnowledgeGraph();
    }

    /**
     * 验证知识图谱JSON格式
     */
    private boolean isValidKnowledgeGraphJson(String jsonStr) {
        try {
            JsonNode json = objectMapper.readTree(jsonStr);

            // 检查必要字段
            return json.has("nodes") && json.has("links") &&
                    json.get("nodes").isArray() && json.get("links").isArray();

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取空的知识图谱JSON
     */
    private String getEmptyKnowledgeGraph() {
        return """
            {
                "nodes": [],
                "links": [],
                "categories": [
                    {"name": "课程", "itemStyle": {"color": "#FF6B6B"}},
                    {"name": "章节", "itemStyle": {"color": "#4ECDC4"}},
                    {"name": "概念", "itemStyle": {"color": "#45B7D1"}},
                    {"name": "关系", "itemStyle": {"color": "#96CEB4"}}
                ]
            }
            """;
    }

    /**
     * 生成示例知识图谱（用于测试）
     */
    public String generateSampleKnowledgeGraph(String courseName) {
        return String.format("""
            {
                "nodes": [
                    {
                        "id": "course_%s",
                        "name": "%s",
                        "category": "课程",
                        "symbolSize": 80,
                        "itemStyle": {"color": "#FF6B6B"},
                        "label": {"show": true}
                    },
                    {
                        "id": "chapter_1",
                        "name": "第一章",
                        "category": "章节",
                        "symbolSize": 60,
                        "itemStyle": {"color": "#4ECDC4"},
                        "label": {"show": true}
                    },
                    {
                        "id": "concept_1",
                        "name": "基础概念",
                        "category": "概念",
                        "symbolSize": 40,
                        "itemStyle": {"color": "#45B7D1"},
                        "label": {"show": true}
                    },
                    {
                        "id": "concept_2",
                        "name": "核心原理",
                        "category": "概念",
                        "symbolSize": 40,
                        "itemStyle": {"color": "#45B7D1"},
                        "label": {"show": true}
                    }
                ],
                "links": [
                    {
                        "source": "course_%s",
                        "target": "chapter_1",
                        "name": "包含",
                        "lineStyle": {"color": "#999"}
                    },
                    {
                        "source": "chapter_1",
                        "target": "concept_1",
                        "name": "包含",
                        "lineStyle": {"color": "#999"}
                    },
                    {
                        "source": "chapter_1",
                        "target": "concept_2",
                        "name": "包含",
                        "lineStyle": {"color": "#999"}
                    },
                    {
                        "source": "concept_1",
                        "target": "concept_2",
                        "name": "关联",
                        "lineStyle": {"color": "#FF9999"}
                    }
                ],
                "categories": [
                    {"name": "课程", "itemStyle": {"color": "#FF6B6B"}},
                    {"name": "章节", "itemStyle": {"color": "#4ECDC4"}},
                    {"name": "概念", "itemStyle": {"color": "#45B7D1"}},
                    {"name": "关系", "itemStyle": {"color": "#96CEB4"}}
                ]
            }
            """, courseName, courseName, courseName);
    }

    /**
     * 测试Dify连接
     */
    public boolean testDifyConnection() {
        try {
            // 发送简单的测试请求
            Map<String, Object> testRequest = new HashMap<>();
            testRequest.put("query", "测试连接");
            testRequest.put("user", "test-user");
            testRequest.put("response_mode", "blocking");
            testRequest.put("conversation_id", "");

            String jsonBody = objectMapper.writeValueAsString(testRequest);
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(difyApiBaseUrl + "/chat-messages")
                    .addHeader("Authorization", "Bearer " + difyApiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Dify连接测试成功");
                    return true;
                } else {
                    System.err.println("Dify连接测试失败: " + response.code() + " " + response.message());
                    return false;
                }
            }

        } catch (IOException e) {
            System.err.println("Dify连接测试异常: " + e.getMessage());
            return false;
        }
    }
}