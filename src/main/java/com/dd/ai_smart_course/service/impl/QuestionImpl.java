package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.service.dto.OptionDTO;
import com.dd.ai_smart_course.service.dto.QuestionDTO;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.entity.Option;
import com.dd.ai_smart_course.mapper.QuestionMapper;
import com.dd.ai_smart_course.mapper.OptionMapper;
import com.dd.ai_smart_course.service.base.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private OptionMapper optionMapper;

    @Override
    public void createQuestion(QuestionDTO questionDTO){
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);//复制过去

        questionMapper.insert(question);

        //将optionDTO转换成Option
        List<Option> options = questionDTO.getOptions().stream().map(optDTO -> {
            Option opt = new Option();
            opt.setQuestionId(question.getId());
            opt.setOptKey(optDTO.getOptKey());
            opt.setOptValue(optDTO.getOptValue());
            return opt;
        }).collect(Collectors.toList());

        optionMapper.insertBatch(options);
    }

    @Override
    public void updateQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);
        questionMapper.update(question);

        optionMapper.deleteByQuestionId(question.getId());
        List<Option> options = questionDTO.getOptions().stream().map(optDTO -> {
            Option opt = new Option();
            opt.setQuestionId(question.getId());
            opt.setOptKey(optDTO.getOptKey());
            opt.setOptValue(optDTO.getOptValue());
            return opt;
        }).collect(Collectors.toList());
        optionMapper.insertBatch(options);
    }

    @Override
    public void deleteQuestion(int id) {
        questionMapper.delete(id);
        optionMapper.deleteByQuestionId(id);
    }

    @Override
    public void deleteQuestions(List<Integer> ids) {
        optionMapper.deleteByQuestionIds(ids);
        questionMapper.deleteBatch(ids);
    }

    @Override
    public QuestionDTO getQuestion(int id) {
        Question question = questionMapper.findById(id);
        List<Option> options = optionMapper.findByQuestionId(id);
        List<OptionDTO> optionDTOs = options.stream().map(opt -> new OptionDTO(opt.getOptKey(), opt.getOptValue())).toList();

        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        dto.setOptions(optionDTOs);
        return dto;
    }

    @Override
    public List<QuestionDTO> listByCourse(int courseId) {
        List<Question> questions = questionMapper.findByCourseId(courseId);
        return questions.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(q, dto);
            List<Option> opts = optionMapper.findByQuestionId(q.getId());
            List<OptionDTO> optionDTOs = opts.stream().map(o -> new OptionDTO(o.getOptKey(), o.getOptValue())).toList();
            dto.setOptions(optionDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<QuestionDTO> listByChapter(int chapterId) {
        List<Question> questions = questionMapper.findByChapterId(chapterId);
        return questions.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(q, dto);
            List<Option> opts = optionMapper.findByQuestionId(q.getId());
            List<OptionDTO> optionDTOs = opts.stream().map(o -> new OptionDTO(o.getOptKey(), o.getOptValue())).toList();
            dto.setOptions(optionDTOs);
            return dto;
        }).collect(Collectors.toList());
    }
}
