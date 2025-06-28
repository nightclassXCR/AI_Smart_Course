package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.ChapterMapper;
import com.dd.ai_smart_course.mapper.ConceptMapper;
import com.dd.ai_smart_course.service.base.ConceptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ConceptImpl implements ConceptService {

    @Autowired
    private ConceptMapper conceptMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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

    @Override
    public void linkConceptToQuestion(int conceptId, int questionId) {
        conceptMapper.linkConceptToQuestion(conceptId, questionId);
    }

    @Override
    public List<Question> getQuestionsByConcept(int conceptId) {
        return conceptMapper.getQuestionsByConcept(conceptId);
    }

    @Override
    public void updateMasteryLevel(int userId, int conceptId, int masteryLevel) {
        conceptMapper.updateMasteryLevel(userId, conceptId, masteryLevel);
    }

    @Override
    public int getMasteryLevel(int userId, int conceptId) {
        Integer masteryLevel = conceptMapper.getMasteryLevel(userId, conceptId);
        return masteryLevel == null ? 0 : masteryLevel;
    }

//    @Override
//    public Map<Concept, Integer> getUserConceptMasteryByCourse(Long userId, Long courseId) {
//        return Map.of();
//    }
//
//    @Override
//    public List<Concept> recommendConceptsForUser(Long userId, Long courseId) {
//        return List.of();
//    }

    @Override
    @Transactional
    public void viewConcept(int conceptId, int userId, Integer durationSeconds) {
        Optional<Concept> conceptOptional = conceptMapper.findById(conceptId);
        if (conceptOptional.isEmpty()) {
            throw new IllegalArgumentException("Concept not found: " + conceptId);
        }

        System.out.println("User " + userId + " viewed concept " + conceptId + " for " + durationSeconds + " seconds.");

        // **修正：发布用户查看概念事件，使用 'view'**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "concept",
                conceptId,
                "view",      // actionType: 使用 'view'
                durationSeconds,
                "{\"action\":\"VIEW_CONCEPT\", \"description\":\"用户查看了概念详情: " + conceptId + "\"}" // detail
        ));
    }

    @Override
    @Transactional
    public void markConceptAsMastered(int conceptId, int userId) {
        Optional<Concept> conceptOptional = conceptMapper.findById(conceptId);
        if (conceptOptional.isEmpty()) {
            throw new IllegalArgumentException("Concept not found: " + conceptId);
        }

        System.out.println("User " + userId + " marked concept " + conceptId + " as mastered.");

        // **修正：发布概念标记掌握事件，使用 'click' 并用 detail 区分**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "concept",
                conceptId,
                "click",    // actionType: 使用 'click' (因为是UI上的一个点击操作)
                null,
                "{\"action\":\"MARK_MASTERED\", \"description\":\"用户手动标记概念为掌握: " + conceptId + "\"}" // detail
        ));
    }

    /**
     * 修改：不一定实现开始复习事件
     */
    @Override
    @Transactional
    public void startConceptReview(int conceptId, int userId) {
        Optional<Concept> conceptOptional = conceptMapper.findById(conceptId);
        if (conceptOptional.isEmpty()) {
            throw new IllegalArgumentException("Concept not found: " + conceptId);
        }

        System.out.println("User " + userId + " started reviewing concept " + conceptId);

        // **修正：发布开始复习事件，使用 'click' 并用 detail 区分**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "concept",
                conceptId,
                "click",       // actionType: 使用 'click' (点击开始复习)
                null,
                "{\"action\":\"START_REVIEW\", \"description\":\"用户开始复习概念: " + conceptId + "\"}" // detail
        ));
    }
}
