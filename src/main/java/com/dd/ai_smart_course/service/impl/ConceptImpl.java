package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.mapper.ChapterMapper;
import com.dd.ai_smart_course.mapper.ConceptMapper;
import com.dd.ai_smart_course.service.ConceptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConceptImpl implements ConceptService {

    @Autowired
    private ConceptMapper conceptMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public List<Concept> getAllConcepts() {
        return conceptMapper.getAllConcepts();
    }

    @Override
    public List<Concept> getConceptsByChapterId(int chapterId) {
        return conceptMapper.getConceptsByChapterId(chapterId);
    }

    @Override
    public int addConcept(ConceptDTO conceptDto) {
        Integer chapterId = chapterMapper.getCourseIdByCourseName(conceptDto.getChapterName());
        if (chapterId == null) {
            throw new RuntimeException("课程不存在");
        }
        Concept concept = new Concept();
        concept.setChapterId(chapterId);
        concept.setName(conceptDto.getName());
        concept.setDescription(conceptDto.getDescription());
        return conceptMapper.addConcept(concept);
    }

    @Override
    public int updateConcept(ConceptDTO conceptDto) {
        Integer chapterId = chapterMapper.getCourseIdByCourseName(conceptDto.getChapterName());

        if (chapterId == null) {
            throw new RuntimeException("课程不存在：" + conceptDto.getChapterName());
        }

        // 2. 构建 Concept 实体对象
        Concept concept = new Concept();
        concept.setId(conceptDto.getId());
        concept.setChapterId(chapterId);
        concept.setName(conceptDto.getName());
        concept.setDescription(conceptDto.getDescription());

        // 3. 调用 Mapper 更新概念信息
        return conceptMapper.updateConcept(concept);
    }


    @Override
    public int deleteConcept(int id) {
        return conceptMapper.deleteConcept(id);
    }
}
