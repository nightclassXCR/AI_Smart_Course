package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.OptionDTO;
import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.entity.QuestionOption;
import com.dd.ai_smart_course.mapper.QuestionMapper;
import com.dd.ai_smart_course.mapper.QuestionOptionMapper;
import com.dd.ai_smart_course.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class QuestionImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    private QuestionOptionMapper optionMapper;
    @Override
    //拉取所有问题
    public List<Question> getAllQuestions() {
        return this.questionMapper.getAllQuestions();
    }

    @Override
    //通过id获取问题
    public Question getQuestionById(int id) {
        return this.questionMapper.getQuestionById(id);
    }

    @Override
    //添加问题
    public int addQuestion(QuestionDTO questiondto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Question question = new Question();

        question.setContext(questiondto.getContext());
        question.setType(Question.QuestionType.valueOf(questiondto.getType()));
        question.setDifficulty(Question.QuestionDifficulty.valueOf(questiondto.getDifficulty()));
        question.setPoint(questiondto.getPoint());
        question.setCreated_at(now);
        question.setUpdated_at(now);
        questionMapper.addQuestion(question);

        if("choice".equals(questiondto.getType())&&questiondto.getOptions()!=null){
            for(OptionDTO o  : questiondto.getOptions()){
                QuestionOption option = new QuestionOption();
                option.setQuestion_id(question.getId());
                option.setOpt_Key(o.getKey());
                option.setOpt_Value(o.getValue());
                optionMapper.insertOption(option);
            }
        }
        return question.getId();
    }

    @Override
    public int updateQuestion(Question question) {
        return 0;
    }

    @Override
    public int deleteQuestion(int id) {
        return 0;
    }
}
