package com.dd.ai_smart_course.service;

import com.dd.ai_smart_course.entity.Question;

import java.util.List;

public interface QuestionService {

    //拉取问题列表
    List<Question> getAllQuestions();
    // 根据问题id获取问题
    Question getQuestionById(int id);
    // 添加问题
    int addQuestion(Question question);
    // 更新问题
    int updateQuestion(Question question);
    // 删除问题
    int deleteQuestion(int id);
}
