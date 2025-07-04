package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.dataset.CourseKnowledgeExtractor;
import com.dd.ai_smart_course.dataset.DifyKnowledgeGraphService;
import com.dd.ai_smart_course.dataset.DifyKnowledgeUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识图谱API控制器
 * 提供知识图谱相关的REST API接口
 */
@RestController
@RequestMapping("/api/knowledge-graph")
public class KnowledgeGraphController {

    @Autowired
    private DifyKnowledgeGraphService difyKnowledgeGraphService;

    @Autowired
    private CourseKnowledgeExtractor courseKnowledgeExtractor;

    @Autowired
    private DifyKnowledgeUploader difyKnowledgeUploader;

    /**
     * 生成知识图谱
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateKnowledgeGraph(
            @RequestParam("query") String query,
            @RequestParam(value = "userId", required = false, defaultValue = "default") String userId) {

        Map<String, Object> result = new HashMap<>();

        try {
            // 调用Dify服务生成知识图谱
            String graphJson = difyKnowledgeGraphService.generateKnowledgeGraphJson(query, userId);

            result.put("success", true);
            result.put("data", graphJson);
            result.put("message", "知识图谱生成成功");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("data", difyKnowledgeGraphService.generateSampleKnowledgeGraph("示例课程"));
            result.put("message", "知识图谱生成失败，返回示例数据: " + e.getMessage());

            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取示例知识图谱
     */
    @GetMapping("/sample")
    public ResponseEntity<Map<String, Object>> getSampleKnowledgeGraph(
            @RequestParam(value = "courseName", required = false, defaultValue = "示例课程") String courseName) {

        Map<String, Object> result = new HashMap<>();

        try {
            String sampleGraph = difyKnowledgeGraphService.generateSampleKnowledgeGraph(courseName);

            result.put("success", true);
            result.put("data", sampleGraph);
            result.put("message", "示例知识图谱获取成功");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("data", "{}");
            result.put("message", "示例知识图谱获取失败: " + e.getMessage());

            return ResponseEntity.ok(result);
        }
    }

    /**
     * 同步数据库数据到Dify知识库
     */
    @PostMapping("/sync-database")
    public ResponseEntity<Map<String, Object>> syncDatabaseToKnowledgeBase() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 检查Dify知识库状态
            if (!difyKnowledgeUploader.checkDatasetStatus()) {
                result.put("success", false);
                result.put("message", "Dify知识库状态检查失败");
                return ResponseEntity.ok(result);
            }

            // 2. 从数据库提取知识文档
            List<Map<String, String>> documents = courseKnowledgeExtractor.extractKnowledgeDocuments();

            if (documents.isEmpty()) {
                result.put("success", false);
                result.put("message", "没有找到可同步的数据");
                return ResponseEntity.ok(result);
            }

            // 3. 批量上传到Dify知识库
            difyKnowledgeUploader.uploadDocuments(documents);

            // 4. 获取统计信息
            Map<String, Integer> stats = courseKnowledgeExtractor.getStatistics();

            result.put("success", true);
            result.put("message", "数据同步完成");
            result.put("documentsUploaded", documents.size());
            result.put("statistics", stats);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "数据同步失败: " + e.getMessage());

            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取数据库统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getDatabaseStatistics() {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Integer> stats = courseKnowledgeExtractor.getStatistics();

            result.put("success", true);
            result.put("data", stats);
            result.put("message", "统计信息获取成功");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "统计信息获取失败: " + e.getMessage());

            return ResponseEntity.ok(result);
        }
    }

    /**
     * 测试Dify连接
     */
    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testDifyConnection() {
        Map<String, Object> result = new HashMap<>();

        try {
            boolean isConnected = difyKnowledgeGraphService.testDifyConnection();

            result.put("success", isConnected);
            result.put("message", isConnected ? "Dify连接正常" : "Dify连接失败");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Dify连接测试异常: " + e.getMessage());

            return ResponseEntity.ok(result);
        }
    }

    /**
     * 根据课程名称生成知识图谱
     */
    @GetMapping("/generate-by-course")
    public ResponseEntity<Map<String, Object>> generateKnowledgeGraphByCourse(
            @RequestParam("courseName") String courseName,
            @RequestParam(value = "userId", required = false, defaultValue = "default") String userId) {

        Map<String, Object> result = new HashMap<>();

        try {
            String query = String.format("请生成关于课程'%s'的知识图谱，包括该课程的章节和核心概念", courseName);
            String graphJson = difyKnowledgeGraphService.generateKnowledgeGraphJson(query, userId);

            result.put("success", true);
            result.put("data", graphJson);
            result.put("message", "课程知识图谱生成成功");
            result.put("courseName", courseName);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("data", difyKnowledgeGraphService.generateSampleKnowledgeGraph(courseName));
            result.put("message", "课程知识图谱生成失败，返回示例数据: " + e.getMessage());
            result.put("courseName", courseName);

            return ResponseEntity.ok(result);
        }
    }

    /**
     * 根据概念名称生成知识图谱
     */
    @GetMapping("/generate-by-concept")
    public ResponseEntity<Map<String, Object>> generateKnowledgeGraphByConcept(
            @RequestParam("conceptName") String conceptName,
            @RequestParam(value = "userId", required = false, defaultValue = "default") String userId) {

        Map<String, Object> result = new HashMap<>();

        try {
            String query = String.format("请生成关于概念'%s'的知识图谱，包括相关的课程、章节和关联概念", conceptName);
            String graphJson = difyKnowledgeGraphService.generateKnowledgeGraphJson(query, userId);

            result.put("success", true);
            result.put("data", graphJson);
            result.put("message", "概念知识图谱生成成功");
            result.put("conceptName", conceptName);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("data", difyKnowledgeGraphService.generateSampleKnowledgeGraph("示例课程"));
            result.put("message", "概念知识图谱生成失败，返回示例数据: " + e.getMessage());
            result.put("conceptName", conceptName);

            return ResponseEntity.ok(result);
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 检查各个服务组件状态
            boolean difyConnected = difyKnowledgeGraphService.testDifyConnection();
            boolean datasetOk = difyKnowledgeUploader.checkDatasetStatus();

            result.put("success", difyConnected && datasetOk);
            result.put("difyConnection", difyConnected);
            result.put("datasetStatus", datasetOk);
            result.put("message", "系统健康检查完成");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "系统健康检查失败: " + e.getMessage());

            return ResponseEntity.ok(result);
        }
    }
}