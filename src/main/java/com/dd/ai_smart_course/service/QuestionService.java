package com.dd.ai_smart_course.service;

import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.entity.Question;

import java.util.List;

public interface QuestionService {

    void createQuestion(QuestionDTO dto);              // 添加题目
    void updateQuestion(QuestionDTO dto);              // 修改题目
    void deleteQuestion(int id);                   // 删除单题
    void deleteQuestions(List<Integer> ids);           // 批量删除
    QuestionDTO getQuestion(int id);               // 查询题目详情
    List<QuestionDTO> listByCourse(int courseId);  // 按课程查题
    List<QuestionDTO> listByChapter(int chapterId);
    List<QuestionDTO> listById(List< Integer> ids);
}
