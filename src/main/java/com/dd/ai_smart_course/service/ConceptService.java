package com.dd.ai_smart_course.service;

import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Question;

import java.util.List;
import java.util.Map;

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

    // 将知识点与题目绑定
    void linkConceptToQuestion(Long conceptId, Long questionId);

    // 获取一个知识点下所有题目
    List<Question> getQuestionsByConcept(Long conceptId);

    // 更新用户对知识点的掌握度
    void updateMasteryLevel(Long userId, Long conceptId, int masteryLevel);

    // 获取用户掌握度
    int getMasteryLevel(Long userId, Long conceptId);

    // 某用户在课程中的知识点掌握情况
    Map<Concept, Integer> getUserConceptMasteryByCourse(Long userId, Long courseId);

    // 推荐用户重点学习的知识点（根据掌握度、错题等）
    List<Concept> recommendConceptsForUser(Long userId, Long courseId);

}

