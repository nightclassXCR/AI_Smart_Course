package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.ApiResponse;
import com.dd.ai_smart_course.dto.KnowledgeGraphResponse;
import com.dd.ai_smart_course.dto.QuestionnaireResponse;
import com.dd.ai_smart_course.dto.request.CourseRequest;
import com.dd.ai_smart_course.entity.Questionnaire;
import com.dd.ai_smart_course.service.DifyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@CrossOrigin

@RestController
@RequestMapping("/questionnaire")
public class DifyQuestionnaireController {

    @Autowired
    private  DifyService difyService;



    /**
     * 获取问卷数据（前端使用）
     */
    @PostMapping("detail")
    public Mono<ResponseEntity<ApiResponse<QuestionnaireResponse>>> getQuestionnaire(@RequestBody CourseRequest request) {
        String courseName = request.getCourseName();
        if (courseName == null || courseName.trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest()
                    // <<< FIX 1: Explicitly specify generic type >>>
                    .body(new ApiResponse<QuestionnaireResponse>(false, null, "课程名称不能为空！", 400)));
        }

        return difyService.generateQuestionnaire(courseName)
                .map(response -> {

                    if ("succeeded".equals(response.getData().getStatus())) {
                        QuestionnaireResponse questionnaire = difyService.extractQuestionnaireResponse(response);
                        System.out.println("Dify Output: " + response.getData().getOutputs().getText());
                        if ( questionnaire!= null) {
                            return ResponseEntity.ok(
                                    new ApiResponse<>(true, questionnaire, "课程信息查询成功。", 200));
                        } else {
                            // <<< FIX 2: Explicitly specify generic type >>>
                            String errorMessage = "Dify 工作流成功执行，但解析输出数据失败。";
                            System.err.println("Dify Output Parsing Error: " + response.getData().getOutputs().getText());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body(new ApiResponse<QuestionnaireResponse>(false, null, errorMessage, 500));
                        }
                    } else {
                        // <<< FIX 3: Explicitly specify generic type >>>
                        String errorMessage = "Dify 工作流执行失败: " + (response.getData().getError() != null ? response.getData().getError() : "未知错误");
                        System.err.println("Dify Workflow Error: " + errorMessage);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse<QuestionnaireResponse>(false, null, errorMessage, 500));
                    }

                }).onErrorResume(e -> {
                    // <<< FIX 4: Explicitly specify generic type >>>
                    String errorMessage = "后端服务错误: " + e.getMessage();
                    System.err.println("Backend Error calling Dify: " + errorMessage);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponse<QuestionnaireResponse>(false, null, errorMessage, 500)));
                });

    }

    /**
     * 提交问卷答案。
     * 通常，提交问卷后会根据答案进行后续处理，例如生成学习推荐或知识图谱。
     * 这里假设我们利用 Dify 服务根据问卷答案生成一个知识图谱。
     *
     * @param payload 包含问卷答案的请求体，例如 {"courseName": "...", "answers": {"q1": "...", "q2": "..."}}
     * @return 包含操作结果的响应，通常是生成的知识图谱数据或处理结果。
     */
    //@PostMapping("/submit")
//    public Mono<ResponseEntity<ApiResponse<KnowledgeGraphResponse>>> submitAnswers(@RequestBody Map<String, Object> payload) {
//        // 从请求体中提取课程名称和答案
//        String courseName = (String) payload.get("courseName");
//        // 假设答案是一个键值对（问题ID/问题文本 -> 答案文本）的Map
//        // 如果前端发送的是 List<Map<String, String>> 或其他结构，需要相应调整此处的数据类型和解析逻辑
//        Map<String, String> answers = (Map<String, String>) payload.get("answers");
//
//        // 对接收到的数据进行基本校验
//        if (courseName == null || courseName.trim().isEmpty()) {
//            return Mono.just(ResponseEntity.badRequest()
//                    .body(new ApiResponse<>(false, null, "课程名称不能为空！", 400)));
//        }
//        if (answers == null || answers.isEmpty()) {
//            return Mono.just(ResponseEntity.badRequest()
//                    .body(new ApiResponse<>(false, null, "提交的答案不能为空！", 400)));
//        }
//
//        // 将答案格式化为适合 Dify 工作流的提示文本
//        // Dify 工作流可能需要一个统一的字符串作为输入来理解用户提交的答案
//        StringBuilder answersPromptBuilder = new StringBuilder();
//        answersPromptBuilder.append("用户提交的课程名称是: ").append(courseName).append("\n");
//        answersPromptBuilder.append("用户提交的问卷答案如下：\n");
//        answers.forEach((questionKey, answerValue) ->
//                answersPromptBuilder.append("问题 ").append(questionKey).append(": ").append(answerValue).append("\n")
//        );
//        String fullPrompt = answersPromptBuilder.toString();
//
//        // 调用 DifyService 来生成知识图谱或其他基于答案的后续内容
//        // 假设 DifyService 中有一个名为 generateKnowledgeGraph 的方法来处理此逻辑
//        return difyService.generateKnowledgeGraph(fullPrompt)
//                .map(response -> {
//                    // 检查 Dify 工作流的执行状态
//                    if ("succeeded".equals(response.getData().getStatus())) {
//                        // 如果成功，尝试提取知识图谱数据
//                        KnowledgeGraphResponse knowledgeGraph = difyService.extractKnowledgeGraphResponse(response);
//                        if (knowledgeGraph != null) {
//                            // 成功提取数据，返回成功响应
//                            return ResponseEntity.ok(
//                                    new ApiResponse<>(true, knowledgeGraph, "问卷提交成功，知识图谱正在生成。", 200));
//                        } else {
//                            // Dify 工作流成功，但解析输出数据失败
//                            String errorMessage = "Dify 工作流成功执行，但解析知识图谱数据失败。";
//                            System.err.println("Dify Knowledge Graph Parsing Error: " + response.getData().getOutputs().getText());
//                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                    .body(new ApiResponse<>(false, null, errorMessage, 500));
//                        }
//                    } else {
//                        // Dify 工作流执行失败，返回错误信息
//                        String errorMessage = "Dify 工作流执行失败: " + (response.getData().getError() != null ? response.getData().getError() : "未知错误");
//                        System.err.println("Dify Workflow Error: " + errorMessage);
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                .body(new ApiResponse<>(false, null, errorMessage, 500));
//                    }
//                }).onErrorResume(e -> {
//                    // 后端服务调用 Dify 时发生异常
//                    String errorMessage = "后端服务错误: " + e.getMessage();
//                    System.err.println("Backend Error calling Dify for knowledge graph: " + errorMessage);
//                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .body(new ApiResponse<>(false, null, errorMessage, 500)));
//                });
//    }
}
