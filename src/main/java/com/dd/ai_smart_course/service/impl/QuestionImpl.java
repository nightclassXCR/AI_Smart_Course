package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.OptionDTO;
import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.entity.Option;
import com.dd.ai_smart_course.mapper.ConceptMapper;
import com.dd.ai_smart_course.mapper.QuestionMapper;
import com.dd.ai_smart_course.mapper.OptionMapper;
import com.dd.ai_smart_course.service.base.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private OptionMapper optionMapper;
    @Autowired
    private ConceptMapper conceptMapper;

    @Override
    public void createQuestion(QuestionDTO questionDTO) {
        validateQuestionDTO(questionDTO);

        // 1. 解析知识点名称，获取概念信息
        List<String> conceptNames = parseConceptNames(questionDTO.getConceptNames());
        List<Concept> concepts = getConceptsByNames(conceptNames);

        if (concepts.isEmpty()) {
            throw new IllegalArgumentException("未找到匹配的知识点概念");
        }

        // 2. 从第一个概念获取章节ID和课程ID（假设同一题目的知识点都属于同一章节）
        Concept primaryConcept = concepts.get(0);
        int chapterId = primaryConcept.getChapterId();
        int courseId = getCourseIdByChapterId(chapterId);

        // 3. 创建Question实体
        Question question = new Question();
        question.setContent(questionDTO.getContent());
        question.setDifficulty(parseDifficultyFromString(questionDTO.getDifficulty()));
        question.setAnswer(questionDTO.getAnswer());
        question.setPoint(questionDTO.getPoint());
        question.setChapterId(chapterId);
        question.setCourseId(courseId);
        // 设置时间戳
        Timestamp now = new Timestamp(System.currentTimeMillis());
        question.setCreatedAt(now);
        question.setUpdatedAt(now);
        System.out.println("question: " + question);
        // 4. 插入题目
        questionMapper.insert(question);
        int questionId = question.getId();
        System.out.println("questionId: " + questionId);

        // 5. 批量插入选项
        if (!CollectionUtils.isEmpty(questionDTO.getOptions())) {
            List<Option> options = questionDTO.getOptions().stream().map(optDTO -> {
                Option opt = new Option();
                opt.setQuestion_id(questionId);
                opt.setOptKey(optDTO.getOptKey());
                opt.setOptValue(optDTO.getOptValue());
                return opt;
            }).collect(Collectors.toList());
            System.out.println("options: " + options);
            optionMapper.insertBatch(options);
        }

        // 6. 关联题目与知识点概念
        List<Integer> conceptIds = concepts.stream()
                .map(Concept::getId)
                .collect(Collectors.toList());
        questionMapper.linkQuestionToConcepts(questionId, conceptIds);
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
                opt.setOptKey(optDTO.getOptKey());
                opt.setOptValue(optDTO.getOptValue());
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
        if (!StringUtils.hasText(questionDTO.getContent())) {
            throw new IllegalArgumentException("题目内容不能为空");
        }
        if (!StringUtils.hasText(questionDTO.getAnswer())) {
            throw new IllegalArgumentException("题目答案不能为空");
        }
        if (CollectionUtils.isEmpty(questionDTO.getConceptNames())) {
            throw new IllegalArgumentException("必须选择至少一个知识点");
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
                .map(opt -> new OptionDTO(opt.getOptKey(), opt.getOptValue()))
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
    /**
     * 解析知识点名称字符串（支持逗号分隔的多个知识点）
     */
    private List<String> parseConceptNames(List<String> conceptNames) {
        if (CollectionUtils.isEmpty(conceptNames)) {
            return Collections.emptyList();
        }

        // 如果前端传入的是单个字符串包含多个知识点（逗号分隔）
        List<String> result = new ArrayList<>();
        for (String nameStr : conceptNames) {
            if (StringUtils.hasText(nameStr)) {
                String[] names = nameStr.split("[,，]"); // 支持中英文逗号
                for (String name : names) {
                    String trimmedName = name.trim();
                    if (StringUtils.hasText(trimmedName)) {
                        result.add(trimmedName);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据知识点名称获取概念实体列表
     */
    /**
     * 根据知识点名称获取概念实体列表（优化为批量查询）
     */
    private List<Concept> getConceptsByNames(List<String> conceptNames) {
        if (CollectionUtils.isEmpty(conceptNames)) {
            return Collections.emptyList();
        }

        // 使用批量查询提高性能
        List<Concept> concepts = conceptMapper.getConceptsByNames(conceptNames);

        // 检查是否有未找到的知识点
        if (concepts.size() != conceptNames.size()) {
            Set<String> foundNames = concepts.stream()
                    .map(Concept::getName)
                    .collect(Collectors.toSet());

            List<String> notFoundNames = conceptNames.stream()
                    .filter(name -> !foundNames.contains(name))
                    .collect(Collectors.toList());

            if (!notFoundNames.isEmpty()) {
                System.err.println("未找到以下知识点: " + String.join(", ", notFoundNames));
                // 可以选择抛出异常或者仅使用找到的概念
                // throw new IllegalArgumentException("未找到知识点: " + String.join(", ", notFoundNames));
            }
        }

        return concepts;
    }


    /**
     * 根据章节ID获取课程ID
     */
    private int getCourseIdByChapterId(int chapterId) {
        Integer courseId = conceptMapper.getCourseIdByChapterId(chapterId);
        if (courseId == null) {
            throw new IllegalArgumentException("未找到章节ID为 " + chapterId + " 的课程信息");
        }
        return courseId;
    }

}