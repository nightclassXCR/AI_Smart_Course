package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.R.ApiResponse;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.dto.KnowledgeGraphResponse;
import com.dd.ai_smart_course.dto.request.CourseRequest;
import com.dd.ai_smart_course.service.DifyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/graph")
public class KnowledgeGraphController {
    @Autowired
    private DifyService difyService;

    @PostMapping("/details")
    public Mono<ResponseEntity<ApiResponse<KnowledgeGraphResponse>>> getCourseDetails(@RequestBody CourseRequest request) {
        String courseName = request.getCourseName();

        if (courseName == null || courseName.trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest()
                    // <<< FIX 1: Explicitly specify generic type >>>
                    .body(new ApiResponse<KnowledgeGraphResponse>(false, null, "课程名称不能为空！", 400)));
        }

        return difyService.callDifyWorkflow(courseName)
                .map(response -> {
                    if ("succeeded".equals(response.getData().getStatus())) {
                        KnowledgeGraphResponse knowledgeGraph = difyService.extractKnowledgeGraphResponse(response);

                        if (knowledgeGraph != null) {
                            return ResponseEntity.ok(
                                    new ApiResponse<>(true, knowledgeGraph, "课程信息查询成功。", 200));
                        } else {
                            // <<< FIX 2: Explicitly specify generic type >>>
                            String errorMessage = "Dify 工作流成功执行，但解析输出数据失败。";
                            System.err.println("Dify Output Parsing Error: " + response.getData().getOutputs().getText());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body(new ApiResponse<KnowledgeGraphResponse>(false, null, errorMessage, 500));
                        }
                    } else {
                        // <<< FIX 3: Explicitly specify generic type >>>
                        String errorMessage = "Dify 工作流执行失败: " + (response.getData().getError() != null ? response.getData().getError() : "未知错误");
                        System.err.println("Dify Workflow Error: " + errorMessage);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse<KnowledgeGraphResponse>(false, null, errorMessage, 500));
                    }
                })
                .onErrorResume(e -> {
                    // <<< FIX 4: Explicitly specify generic type >>>
                    String errorMessage = "后端服务错误: " + e.getMessage();
                    System.err.println("Backend Error calling Dify: " + errorMessage);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponse<KnowledgeGraphResponse>(false, null, errorMessage, 500)));
                });
    }


}
