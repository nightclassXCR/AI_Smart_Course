package com.dd.ai_smart_course.dataset;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Dify知识库上传服务
 * 负责将本地数据库提取的知识文档上传到Dify知识库
 */
@Service
public class DifyKnowledgeUploader {

    // 从配置文件读取Dify相关配置
    @Value("${dify.api.base-url}")
    private String difyApiBaseUrl;

    @Value("${dify.api.key}")
    private String difyApiKey;

    @Value("${dify.dataset.key}")
    private String difyDatasetKey;

    @Value("${dify.dataset.id}")
    private String difyDatasetId;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DifyKnowledgeUploader() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 上传单个文档到Dify知识库
     */
    public boolean uploadDocument(Map<String, String> document) {
        try {
            String content = document.get("Content");
            if (content == null || content.trim().isEmpty()) {
                System.err.println("文档内容为空，跳过上传");
                return false;
            }

            // 构建 Dify API 请求体
            Map<String, Object> requestBodyMap = new HashMap<>();
            requestBodyMap.put("name", generateDocumentName(document));
            requestBodyMap.put("text", content);
            requestBodyMap.put("indexing_technique", "high_quality");

            // 添加 process_rule
            Map<String, Object> processRule = new HashMap<>();
            processRule.put("mode", "automatic");
            requestBodyMap.put("process_rule", processRule);

            String jsonBody = objectMapper.writeValueAsString(requestBodyMap);
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url("http://82.157.104.71/v1/datasets/" + difyDatasetId + "/document/create-by-text")
                    .addHeader("Authorization", "Bearer " + difyDatasetKey)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("文档上传成功: " + requestBodyMap.get("name"));
                    return true;
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "无错误信息";
                    System.err.println("文档上传失败: " + response.code() + " " + response.message());
                    System.err.println("错误信息: " + errorBody);
                    return false;
                }
            }

        } catch (Exception e) {
            System.err.println("上传文档时发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

//    public boolean uploadDocument(Map<String, String> document) {
//        try {
//            String content = document.get("Content");
//            if (content == null || content.trim().isEmpty()) {
//                System.err.println("文档内容为空，跳过上传");
//                return false;
//            }
//
//            // 构建元数据（移除Content字段）
//            Map<String, String> metadata = new HashMap<>(document);
//            metadata.remove("Content");
//
//            // 构建Dify API请求体
//            Map<String, Object> requestBodyMap = new HashMap<>();
//            requestBodyMap.put("text", content);
//            requestBodyMap.put("doc_form", "text");
//            requestBodyMap.put("doc_language", "zh");
//            requestBodyMap.put("indexing_technique", "high_quality"); // 高质量索引
//            requestBodyMap.put("metadata", metadata);
//
//            // 添加文档标识
//            String docName = generateDocumentName(document);
//            requestBodyMap.put("name", docName);
//
//            String jsonBody = objectMapper.writeValueAsString(requestBodyMap);
//            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
//
//            Request request = new Request.Builder()
//                    .url(difyApiBaseUrl + "/datasets/" + difyDatasetId + "/document/create-by-text")
//                    .addHeader("Authorization", "Bearer " + difyDatasetKey)
//                    .addHeader("Content-Type", "application/json")
//                    .post(body)
//                    .build();
//
//            try (Response response = httpClient.newCall(request).execute()) {
//                if (response.isSuccessful()) {
//                    System.out.println("文档上传成功: " + docName);
//                    return true;
//                } else {
//                    String errorBody = response.body() != null ? response.body().string() : "无错误信息";
//                    System.err.println("文档上传失败: " + response.code() + " " + response.message());
//                    System.err.println("错误信息: " + errorBody);
//                    return false;
//                }
//            }
//
//        } catch (Exception e) {
//            System.err.println("上传文档时发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * 批量上传文档到Dify知识库
     */
    public void uploadDocuments(List<Map<String, String>> documents) {
        int successCount = 0;
        int failCount = 0;

        System.out.println("开始批量上传文档，总数：" + documents.size());

        for (int i = 0; i < documents.size(); i++) {
            Map<String, String> document = documents.get(i);

            System.out.println("正在上传文档 " + (i + 1) + "/" + documents.size());

            if (uploadDocument(document)) {
                successCount++;
            } else {
                failCount++;
            }

            // 避免API限制，每次上传后稍作延迟
            try {
                Thread.sleep(1000); // 1秒延迟
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("批量上传完成！成功：" + successCount + "，失败：" + failCount);
    }

    /**
     * 生成文档名称
     */
    private String generateDocumentName(Map<String, String> document) {
        String entityType = document.get("EntityType");
        String docType = document.get("DocumentType");

        switch (entityType) {
            case "Course":
                return "课程-" + document.get("CourseName");
            case "Chapter":
                return "章节-" + document.get("CourseName") + "-" + document.get("ChapterTitle");
            case "Concept":
                return "概念-" + document.get("CourseName") + "-" + document.get("ConceptName");
            case "Relationship":
                return "关系-" + document.get("Concept1Name") + "-" + document.get("Concept2Name");
            default:
                return docType + "-" + document.get("EntityId");
        }
    }

    /**
     * 检查知识库状态
     */
    public boolean checkDatasetStatus() {
        try {
            Request request = new Request.Builder()
                    .url(difyApiBaseUrl + "/datasets/" + difyDatasetId)
                    .addHeader("Authorization", "Bearer " + difyDatasetKey)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    System.out.println("知识库状态检查成功");
                    System.out.println("知识库信息: " + responseBody);
                    return true;
                } else {
                    System.err.println("知识库状态检查失败: " + response.code() + " " + response.message());
                    return false;
                }
            }

        } catch (IOException e) {
            System.err.println("检查知识库状态时发生异常: " + e.getMessage());
            return false;
        }
    }

    /**
     * 清空知识库（谨慎使用）
     */
    public boolean clearDataset() {
        try {
            // 首先获取所有文档
            Request getRequest = new Request.Builder()
                    .url(difyApiBaseUrl + "/datasets/" + difyDatasetId + "/documents")
                    .addHeader("Authorization", "Bearer " + difyDatasetKey)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(getRequest).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    // 这里需要解析响应，获取文档ID列表，然后逐个删除
                    // 具体实现取决于Dify API的返回格式
                    System.out.println("知识库清空功能需要根据具体API实现");
                    return true;
                } else {
                    System.err.println("获取知识库文档列表失败: " + response.code());
                    return false;
                }
            }

        } catch (IOException e) {
            System.err.println("清空知识库时发生异常: " + e.getMessage());
            return false;
        }
    }

    /**
     * 更新知识库配置
     */
    public boolean updateDatasetConfig(Map<String, Object> config) {
        try {
            String jsonBody = objectMapper.writeValueAsString(config);
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(difyApiBaseUrl + "/datasets/" + difyDatasetId)
                    .addHeader("Authorization", "Bearer " + difyDatasetKey)
                    .addHeader("Content-Type", "application/json")
                    .patch(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("知识库配置更新成功");
                    return true;
                } else {
                    System.err.println("知识库配置更新失败: " + response.code() + " " + response.message());
                    return false;
                }
            }

        } catch (IOException e) {
            System.err.println("更新知识库配置时发生异常: " + e.getMessage());
            return false;
        }
    }
}