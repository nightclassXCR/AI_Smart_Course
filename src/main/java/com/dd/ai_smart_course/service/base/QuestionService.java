package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.dto.QuestionDTO;
import java.util.List;

public interface QuestionService {

    /**
     * 创建问题
     */
    void createQuestion(QuestionDTO questionDTO);

    /**
     * 更新问题
     */
    void updateQuestion(QuestionDTO questionDTO);

    /**
     * 删除单个问题
     */
    void deleteQuestion(int id);

    /**
     * 批量删除问题
     */
    void deleteQuestions(List<Integer> ids);

    /**
     * 根据ID获取问题详情
     */
    QuestionDTO getQuestion(int id);

    /**
     * 获取所有问题列表
     */
    List<QuestionDTO> getAllQuestions();

    /**
     * 根据课程ID获取问题列表
     */
    List<QuestionDTO> listByCourse(int courseId);

    /**
     * 根据章节ID获取问题列表
     */
    List<QuestionDTO> listByChapter(int chapterId);

    /**
     * 根据问题ID列表获取问题
     */
    List<QuestionDTO> listById(List<Integer> ids);

    /**
     * 根据概念ID获取相关问题列表
     */
    List<QuestionDTO> listByConcept(int conceptId);
}