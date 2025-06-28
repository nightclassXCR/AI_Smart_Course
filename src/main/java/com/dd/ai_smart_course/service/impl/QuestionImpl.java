package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.OptionDTO;
import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.entity.Option;
import com.dd.ai_smart_course.mapper.QuestionMapper;
import com.dd.ai_smart_course.mapper.OptionMapper;
import com.dd.ai_smart_course.service.base.QuestionService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private OptionMapper optionMapper;

    @Override
    public void createQuestion(QuestionDTO questionDTO) {
        validateQuestionDTO(questionDTO);

        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);

        // DTO的difficulty是String，需要转换为枚举保存到实体
        if (questionDTO.getDifficulty() != null) {
            question.setDifficulty(parseDifficultyFromString(questionDTO.getDifficulty()));
        }

        questionMapper.insert(question);

        // 批量插入选项
        if (!CollectionUtils.isEmpty(questionDTO.getOptions())) {
            List<Option> options = questionDTO.getOptions().stream().map(optDTO -> {
                Option opt = new Option();
                opt.setQuestionId(question.getId());
                opt.setOptKey(optDTO.getOptKey());
                opt.setOptValue(optDTO.getOptValue());
                return opt;
            }).collect(Collectors.toList());

            optionMapper.insertBatch(options);
        }
    }

    @Override
    public void updateQuestion(QuestionDTO questionDTO) {
        validateQuestionDTO(questionDTO);

        // 检查问题是否存在
        Question existingQuestion = questionMapper.findById(questionDTO.getId());
        if (existingQuestion == null) {
            throw new IllegalArgumentException("Question not found with id: " + questionDTO.getId());
        }

        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);

        // DTO的difficulty是String，需要转换为枚举保存到实体
        if (questionDTO.getDifficulty() != null) {
            question.setDifficulty(parseDifficultyFromString(questionDTO.getDifficulty()));
        }

        questionMapper.update(question);

        // 先删除旧选项，再插入新选项
        optionMapper.deleteByQuestionId(question.getId());

        if (!CollectionUtils.isEmpty(questionDTO.getOptions())) {
            List<Option> options = questionDTO.getOptions().stream().map(optDTO -> {
                Option opt = new Option();
                opt.setQuestionId(question.getId());
                opt.setOptKey(optDTO.getOptKey());
                opt.setOptValue(optDTO.getOptValue());
                return opt;
            }).collect(Collectors.toList());

            optionMapper.insertBatch(options);
        }
    }

    @Override
    public void deleteQuestion(int id) {
        Question existingQuestion = questionMapper.findById(id);
        if (existingQuestion == null) {
            throw new IllegalArgumentException("Question not found with id: " + id);
        }

        // 先删除选项，再删除问题
        optionMapper.deleteByQuestionId(id);
        questionMapper.delete(id);
    }

    @Override
    public void deleteQuestions(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        // 批量删除选项和问题
        optionMapper.deleteByQuestionIds(ids);
        questionMapper.deleteBatch(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDTO getQuestion(int id) {
        Question question = questionMapper.findById(id);
        if (question == null) {
            throw new IllegalArgumentException("Question not found with id: " + id);
        }

        List<Option> options = optionMapper.findByQuestionId(id);
        return convertToQuestionDTO(question, options);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> listByCourse(int courseId) {
        List<Question> questions = questionMapper.findByCourseId(courseId);
        return convertToQuestionDTOList(questions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> listByChapter(int chapterId) {
        List<Question> questions = questionMapper.findByChapterId(chapterId);
        return convertToQuestionDTOList(questions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> listById(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        List<Question> questions = questionMapper.findByIds(ids);
        return convertToQuestionDTOList(questions);
    }

    /**
     * 验证QuestionDTO参数
     */
    private void validateQuestionDTO(QuestionDTO questionDTO) {
        if (questionDTO == null) {
            throw new IllegalArgumentException("QuestionDTO cannot be null");
        }
        if (questionDTO.getContent() == null || questionDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Question content cannot be empty");
        }
        if (questionDTO.getCourseId() <= 0) {
            throw new IllegalArgumentException("Course ID must be positive");
        }
        if (questionDTO.getChapterId() <= 0) {
            throw new IllegalArgumentException("Chapter ID must be positive");
        }
    }

    /**
     * 将字符串转换为难度枚举
     */
    private Question.QuestionDifficulty parseDifficultyFromString(String difficultyStr) {
        // 这里需要根据实际的枚举类型进行转换
        // 假设枚举类型为 DifficultyEnum
        try {
            return Question.QuestionDifficulty.valueOf(difficultyStr.toLowerCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid difficulty value: " + difficultyStr);
        }
    }

    /**
     * 将Question实体转换为QuestionDTO
     */
    private QuestionDTO convertToQuestionDTO(Question question, List<Option> options) {
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);

        // 将枚举转换为字符串
        if (question.getDifficulty() != null) {
            dto.setDifficulty(question.getDifficulty().toString());
        }

        // 转换选项
        List<OptionDTO> optionDTOs = options.stream()
                .map(opt -> new OptionDTO(opt.getOptKey(), opt.getOptValue()))
                .collect(Collectors.toList());
        dto.setOptions(optionDTOs);

        return dto;
    }

    /**
     * 批量转换Question列表为QuestionDTO列表
     */
    private List<QuestionDTO> convertToQuestionDTOList(List<Question> questions) {
        if (CollectionUtils.isEmpty(questions)) {
            return Collections.emptyList();
        }

        return questions.stream().map(q -> {
            List<Option> options = optionMapper.findByQuestionId(q.getId());
            return convertToQuestionDTO(q, options);
        }).collect(Collectors.toList());
    }
}