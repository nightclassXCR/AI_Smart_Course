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

import java.util.*;
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
                opt.setQuestion_id(question.getId());
                opt.setOpt_Key(optDTO.getOptKey());
                opt.setOpt_Value(optDTO.getOptValue());
                return opt;
            }).collect(Collectors.toList());

            optionMapper.insertBatch(options);
        }

        // 关联概念知识点
        if (!CollectionUtils.isEmpty(questionDTO.getConceptIds())) {
            questionMapper.linkQuestionToConcepts(question.getId(), questionDTO.getConceptIds());
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
                opt.setQuestion_id(question.getId());
                opt.setOpt_Key(optDTO.getOptKey());
                opt.setOpt_Value(optDTO.getOptValue());
                return opt;
            }).collect(Collectors.toList());

            optionMapper.insertBatch(options);
        }

        // 更新概念关联：先删除旧关联，再插入新关联
        questionMapper.deleteQuestionConceptLinks(question.getId());
        if (!CollectionUtils.isEmpty(questionDTO.getConceptIds())) {
            questionMapper.linkQuestionToConcepts(question.getId(), questionDTO.getConceptIds());
        }
    }

    @Override
    public void deleteQuestion(int id) {
        Question existingQuestion = questionMapper.findById(id);
        if (existingQuestion == null) {
            throw new IllegalArgumentException("Question not found with id: " + id);
        }

        // 先删除概念关联、选项，再删除问题
        questionMapper.deleteQuestionConceptLinks(id);
        optionMapper.deleteByQuestionId(id);
        questionMapper.delete(id);
    }

    @Override
    public void deleteQuestions(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        // 批量删除概念关联、选项和问题
        for (Integer id : ids) {
            questionMapper.deleteQuestionConceptLinks(id);
        }
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

        // 获取概念信息
        List<Map<String, Object>> concepts = questionMapper.getConceptsByQuestionId(id);

        return convertToQuestionDTO(question, options, concepts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionMapper.getAllQuestions();
        return convertToQuestionDTOList(questions);
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
     * 根据概念查询问题列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> listByConcept(int conceptId) {
        List<Question> questions = questionMapper.findByConceptId(conceptId);
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
        try {
            return Question.QuestionDifficulty.valueOf(difficultyStr.toLowerCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid difficulty value: " + difficultyStr);
        }
    }

    /**
     * 将Question实体转换为QuestionDTO（单个问题，包含概念信息）
     */
    private QuestionDTO convertToQuestionDTO(Question question, List<Option> options, List<Map<String, Object>> concepts) {
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);

        // 将枚举转换为字符串
        if (question.getDifficulty() != null) {
            dto.setDifficulty(question.getDifficulty().toString());
        }

        // 转换选项
        List<OptionDTO> optionDTOs = options.stream()
                .map(opt -> new OptionDTO(opt.getOpt_Key(), opt.getOpt_Value()))
                .collect(Collectors.toList());
        dto.setOptions(optionDTOs);

        // 设置概念信息
        if (!CollectionUtils.isEmpty(concepts)) {
            List<String> conceptNames = concepts.stream()
                    .map(concept -> (String) concept.get("conceptName"))
                    .collect(Collectors.toList());
            List<Integer> conceptIds = concepts.stream()
                    .map(concept -> (Integer) concept.get("conceptId"))
                    .collect(Collectors.toList());

            dto.setConceptNames(conceptNames);
            dto.setConceptIds(conceptIds);
        }

        return dto;
    }

    /**
     * 批量转换Question列表为QuestionDTO列表（优化版本，减少数据库查询）
     */
    private List<QuestionDTO> convertToQuestionDTOList(List<Question> questions) {
        if (CollectionUtils.isEmpty(questions)) {
            return Collections.emptyList();
        }

        // 收集所有问题ID
        List<Integer> questionIds = questions.stream()
                .map(Question::getId)
                .collect(Collectors.toList());

        // 批量获取所有选项
        Map<Integer, List<Option>> optionsMap = new HashMap<>();
        for (Integer questionId : questionIds) {
            List<Option> options = optionMapper.findByQuestionId(questionId);
            optionsMap.put(questionId, options);
        }

        // 批量获取概念信息映射
        Map<Integer, List<Map<String, Object>>> conceptsMap = buildConceptsMap(questionIds);

        // 转换为DTO
        return questions.stream().map(q -> {
            List<Option> options = optionsMap.getOrDefault(q.getId(), Collections.emptyList());
            List<Map<String, Object>> concepts = conceptsMap.getOrDefault(q.getId(), Collections.emptyList());
            return convertToQuestionDTO(q, options, concepts);
        }).collect(Collectors.toList());
    }

    /**
     * 构建问题ID到概念信息列表的映射
     */
    private Map<Integer, List<Map<String, Object>>> buildConceptsMap(List<Integer> questionIds) {
        if (CollectionUtils.isEmpty(questionIds)) {
            return Collections.emptyMap();
        }

        Map<Integer, List<Map<String, Object>>> conceptsMap = new HashMap<>();

        try {
            // 批量查询所有问题的概念信息
            List<Map<String, Object>> results = questionMapper.getConceptsByQuestionIds(questionIds);

            // 按问题ID分组
            for (Map<String, Object> result : results) {
                Integer questionId = (Integer) result.get("questionId");
                Map<String, Object> conceptInfo = new HashMap<>();
                conceptInfo.put("conceptId", result.get("conceptId"));
                conceptInfo.put("conceptName", result.get("conceptName"));

                conceptsMap.computeIfAbsent(questionId, k -> new ArrayList<>()).add(conceptInfo);
            }
        } catch (Exception e) {
            // 如果批量查询失败，回退到逐个查询
            for (Integer questionId : questionIds) {
                List<Map<String, Object>> concepts = questionMapper.getConceptsByQuestionId(questionId);
                conceptsMap.put(questionId, concepts);
            }
        }

        return conceptsMap;
    }
}