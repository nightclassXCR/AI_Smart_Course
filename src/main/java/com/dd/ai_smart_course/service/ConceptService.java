package com.dd.ai_smart_course.service;

import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;

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
}
