package com.dd.ai_smart_course.controller;


import ch.qos.logback.core.util.StringUtil;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.service.base.ConceptService;
import com.dd.ai_smart_course.service.base.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/questionBank")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ConceptService conceptService;

    //上传题目数据
    @GetMapping("/list")
    public Result<Map<String, Object>> getQuestionList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String conceptName,
            @RequestParam(required = false) Integer conceptId,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer chapterId) {

        try {
            List<QuestionDTO> allQuestions;
            if (StringUtils.hasText(conceptName)) {
                List<Concept> concepts = conceptService.getAllConcepts().stream()
                        .filter(concept -> concept.getName().contains(conceptName.trim()))
                        .collect(Collectors.toList());
                if (!concepts.isEmpty()) {
                    allQuestions = questionService.listByConcept(concepts.get(0).getId());
                } else {
                    allQuestions = List.of();
                }

            }else if(conceptId != null){
                allQuestions = questionService.listByConcept(conceptId);
            }else if(courseId != null){
                allQuestions = questionService.listByCourse(courseId);
            }else if(chapterId != null){
                allQuestions = questionService.listByChapter(chapterId);
            }else{
                // ✅ 使用新的 getAllQuestions 方法获取所有题目
                allQuestions = questionService.getAllQuestions();
            }

            // 手动实现分页逻辑
            int total = allQuestions.size();
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, total);

            List<QuestionDTO> pagedQuestions;
            if (startIndex >= total) {
                pagedQuestions = List.of(); // 空列表
            } else {
                pagedQuestions = allQuestions.subList(startIndex, endIndex);
            }

            // 构建返回结果
            Map<String, Object> data = new HashMap<>();

            data.put("total", total);
            data.put("list", pagedQuestions);

            return Result.success( "获取题目列表成功", data);
        } catch (Exception e) {
            log.error("获取题目列表失败", e);
            return Result.error("获取题目列表失败");
        }
    }
    @GetMapping("/concept")
    public Result<Map<String, Object>> searchByConcept(
            @RequestParam String conceptName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize){
        try{
            List<QuestionDTO> allQuestions = List.of();
            if (StringUtils.hasText(conceptName)) {
                // 根据概念名称查找匹配的概念
                List<Concept> matchingConcepts = conceptService.getAllConcepts().stream()
                        .filter(concept -> concept.getName().contains(conceptName.trim()))
                        .collect(Collectors.toList());

                if (!matchingConcepts.isEmpty()) {
                    // 获取所有匹配概念的题目
                    allQuestions = matchingConcepts.stream()
                            .flatMap(concept -> questionService.listByConcept(concept.getId()).stream())
                            .collect(Collectors.toList());
                }
            }
            // 手动实现分页逻辑
            int total = allQuestions.size();
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, total);

            List<QuestionDTO> pagedQuestions;
            if (startIndex >= total) {
                pagedQuestions = List.of();
            } else {
                pagedQuestions = allQuestions.subList(startIndex, endIndex);
            }

            Map<String, Object> result = new HashMap<>();
            Map<String, Object> data = new HashMap<>();

            data.put("list", pagedQuestions);
            data.put("total", total);
            return Result.success("查询成功", data);
        } catch (Exception e) {
            log.error("查询失败", e);
            return Result.error("查询失败");
        }
    }
    /*
     * 创建题目
     */
    @PostMapping("/create")
    public Result<Map<String, Object>> createQuestion(@RequestBody QuestionDTO questionDTO){
        try {
            questionService.createQuestion(questionDTO);

            return Result.success();

        } catch (Exception e) {
            log.error("创建题目失败", e);
            return Result.error("创建题目失败");
        }
    }
    /*
     * 更新题目
     */
    @PostMapping("/update")
    public Result<Map<String, Object>> updateQuestion(@RequestBody QuestionDTO questionDTO){
        log.info("接收更新参数: {}", questionDTO);
        try {
            questionService.updateQuestion(questionDTO);

            return Result.success();

        } catch (Exception e) {
            log.error("更新题目失败", e);
            return Result.error("更新题目失败");
        }
    }

    /*
     * 单个删除题目
     */
    @DeleteMapping("/delete/{questionId}")
    public Result<Map<String, Object>> deleteQuestion(@PathVariable int questionId){
        try {
            questionService.deleteQuestion(questionId);

            return Result.success();

        } catch (Exception e) {
            log.error("删除题目失败", e);
            return Result.error("删除题目失败");
        }
    }
}