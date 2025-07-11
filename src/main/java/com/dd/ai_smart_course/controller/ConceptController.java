package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.service.base.ConceptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concepts")
@Slf4j

public class ConceptController {

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    /**
     * 获取所有概念
     * @return
     */
    @GetMapping
    public Result<List<Concept>> getAllConcepts() {

        return Result.success(conceptService.getAllConcepts());
    }
    /**
     * 获取某个章节的 concept
     * @param chapterId
     * @return
     */
    @GetMapping("/by-chapter/{chapterId}")
    public Result<List<Concept>> getConceptsByChapterId(@PathVariable int chapterId) {
        return Result.success(conceptService.getConceptsByChapterId(chapterId));
    }
    /**
     * 添加 concept
     * @param conceptDto
     * @return
     */
    @PostMapping
    public Result<String> addConcept(@RequestBody ConceptDTO conceptDto) {
        log.info("添加 concept: {}", conceptDto);
        conceptService.addConcept(conceptDto);
        return Result.success("添加成功");
    }

    /**
     * 根据概念ID 获取概念详情
     * @param id
     */
    @GetMapping("/{id}")
    public Result<Concept> getConceptById(@PathVariable int id) {
        log.info("get a request: get concept by conceptID = {}", id);
        return Result.success(conceptService.getConceptById(id));
    }
    /**
     * 更新 concept
     * @param conceptDto
     * @return
     */
    @PutMapping
    public Result<String> updateConcept(@RequestBody ConceptDTO conceptDto) {
        conceptService.updateConcept(conceptDto);
        return Result.success("更新成功");
    }
    /**
     * 删除 concept
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteConcept(@PathVariable int id) {
        if(conceptService.deleteConcept(id) > 0){
            return Result.success("删除成功");
        }else {
            return Result.error("删除失败");
        }
    }

    /**
     * 添加 concept 和 question 的关联关系
     * @param conceptId
     * @param questionId
     */
    @PostMapping("/{conceptId}/questions/{questionId}")
    public Result<String> linkConceptToQuestion(@PathVariable int conceptId, @PathVariable int questionId) {
        conceptService.linkConceptToQuestion(conceptId, questionId);
        return Result.success("关联成功");
    }

    /**
     * 获取某个 concept 的所有 question
     * @param conceptId
     * @return
     */
    @GetMapping("/{conceptId}/questions")
    public Result<List<Question>> getQuestionsByConcept(@PathVariable int conceptId) {
        return Result.success(conceptService.getQuestionsByConcept(conceptId));
    }

    /**
     * 更新 concept 的掌握程度
     * @param userId
     * @param conceptId
     * @param masteryLevel
     */
    @PostMapping("/{userId}/concepts/{conceptId}/mastery/{masteryLevel}")
    public Result<String> updateMasteryLevel(@PathVariable int userId, @PathVariable int conceptId, @PathVariable int masteryLevel) {
        conceptService.updateMasteryLevel(userId, conceptId, masteryLevel);
        return Result.success("更新成功");
    }

    /**
     * 获取某个 concept 的掌握程度
     * @param userId
     * @param conceptId
     * @return
     */
    @GetMapping("/{userId}/concepts/{conceptId}/mastery")
    public Result<Integer> getMasteryLevel(@PathVariable int userId, @PathVariable int conceptId) {
        return Result.success(conceptService.getMasteryLevel(userId, conceptId));
    }

    /**
     * 用户查看某个 concept
     * @param conceptId
     * @param durationSeconds
     * @return
     */
    @PostMapping("/{conceptId}/users/view")
    public Result<String> viewConcept(HttpServletRequest request,@PathVariable int conceptId, @RequestParam Integer durationSeconds) {
        String Header = request.getHeader("Authorization");
        String token = Header != null ? Header.replace("Bearer ", "").trim() : null;
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        conceptService.viewConcept(conceptId, userId, durationSeconds);
        return Result.success("操作成功");
    }

    /**
     * 用户标记某个 concept 为已掌握
     * @param conceptId
     * @param userId
     * @return
     */
    @PostMapping("/{conceptId}/users/{userId}/mastered")
    public Result<String> markConceptAsMastered(@PathVariable int conceptId, @PathVariable int userId) {
        conceptService.markConceptAsMastered(conceptId, userId);
        return Result.success("操作成功");
    }

    /**
     * 用户开始 review 某个 concept
     * @param conceptId
     * @param userId
     * @return
     */
    @PostMapping("/{conceptId}/users/{userId}/review")
    public Result<String> startConceptReview(@PathVariable int conceptId, @PathVariable int userId) {
        conceptService.startConceptReview(conceptId, userId);
        return Result.success("操作成功");
    }
}
