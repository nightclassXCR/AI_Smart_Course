package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.ApiResponse;
import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.dto.KnowledgeGraphResponse;
import com.dd.ai_smart_course.dto.QuestionnaireResponse;
import com.dd.ai_smart_course.dto.request.CourseRequest;
import com.dd.ai_smart_course.dto.request.UserQuizSubmissionRequest;
import com.dd.ai_smart_course.entity.Questionnaire;
import com.dd.ai_smart_course.service.DifyService;
import com.dd.ai_smart_course.service.base.QuizService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.http.HttpRequest;
import java.util.*;

@CrossOrigin

@RestController
@RequestMapping("/questionnaire")
public class DifyQuestionnaireController {

    @Autowired
    private  DifyService difyService;
    @Autowired
    private QuizService quizService;

    private JwtTokenUtil jwtTokenUtil;



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

    @PostMapping("submit")
    public ResponseEntity<String> submitFullQuiz(@RequestBody UserQuizSubmissionRequest submissionRequest) {
        try {

            quizService.processUserQuizSubmission(submissionRequest);
            return ResponseEntity.ok("Quiz submission processed and mastery levels updated successfully.");
        } catch (Exception e) {
            System.err.println("Error processing quiz submission: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process quiz submission: " + e.getMessage());
        }
    }


}
