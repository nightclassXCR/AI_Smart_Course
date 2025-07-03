package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Question;

import java.util.List;

public interface ConceptService {

    // 获取所有概念
    List<Concept> getAllConcepts();

    // 根据章节ID获取概念
    List<Concept> getConceptsByChapterId(int chapterId);

    // 添加概念
    int addConcept(ConceptDTO conceptDto);

    // 更新概念
    int updateConcept(ConceptDTO conceptDto);

    // 删除概念
    int deleteConcept(int id);

    /**
     * 链接概念和题目
     * 建立多对多的对应关系
     * 允许系统知道某个题目考察哪些知识点
     * @param conceptId
     * @param questionId
     */
    void linkConceptToQuestion(int conceptId, int questionId);

    /**
     * 根据概念ID获取题目
     * 允许系统知道某个知识点被哪些题目所使用
     * @param conceptId
     * @return
     */
    List<Question> getQuestionsByConcept(int conceptId);

    /**
     * 更新用户掌握度
     * 记录用户对某个知识点的掌握度
     * 允许系统知道用户对某个知识点掌握程度
     * @param userId
     * @param conceptId
     * @param masteryLevel
     */
    void updateMasteryLevel(int userId, int conceptId, int masteryLevel);

    /**
     * 获取用户掌握度
     * 允许系统知道用户对某个知识点掌握程度
     * @param userId
     * @param conceptId
     * @return
     */
    int getMasteryLevel(int userId, int conceptId);

//    /**
//     * 获取用户对课程掌握的知识点
//     * 允许系统知道用户对某个课程掌握哪些知识点
//     * @param userId
//     * @param courseId
//     * @return
//     */
//    Map<Concept, Integer> getUserConceptMasteryByCourse(Long userId, Long courseId);
//
//    /**
//     * 为用户推荐知识点
//     *
//     * @param userId
//     * @param courseId
//     * @return
//     */
//    List<Concept> recommendConceptsForUser(Long userId, Long courseId);
    // 用户对知识点的浏览、掌握、复习等操作
    void viewConcept(int conceptId, int userId, Integer durationSeconds);
    // 用户对知识点的掌握
    void markConceptAsMastered(int conceptId, int userId);
    // 用户对知识点的复习
    void startConceptReview(int conceptId, int userId);

    /**
     * 获取概念详情
     * @param id
     * @return
     */
    Concept getConceptById(int id);
}

