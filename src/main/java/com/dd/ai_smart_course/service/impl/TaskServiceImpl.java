package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.*;
import com.dd.ai_smart_course.entity.*;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.*;
import com.dd.ai_smart_course.service.base.ConceptMasteryService;
import com.dd.ai_smart_course.service.base.TaskService;
import com.dd.ai_smart_course.service.exception.SQLDataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired

    private TaskQuestionMapper tqMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    ConceptMasteryService conceptMasteryService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private OptionMapper optionMapper;

    @Override
    @Transactional
    public void insert(TaskDTO taskDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        Integer courseId = courseMapper.getCourseIdByCourseName(taskDTO.getCourseName());
        if (courseId==null){
            log.info("Course not found: " + taskDTO.getCourseName());
            throw new SQLDataNotFoundException("课程不存在");

        }
        task.setCreatedAt(LocalDateTime.now());
        task.setType(Task.Type.homework);
        task.setCourseId(courseId);
        UserTask userTask = new UserTask();
        List<Integer> userIds = userMapper.getStudentIdsByCourseId(courseId);
        log.info("insertBatch: {}", task);
        taskMapper.insert(task);
        int taskId = task.getId();
        if(userIds.isEmpty()){
            log.info("No students found in course: " + courseId);
            throw new SQLDataNotFoundException("没有学生");
        }
        taskMapper.insertUserTask(userIds, taskId);
        List<Question> questions = taskDTO.getQuestions();
        for (Question question : questions) {
            Task_question tq = new Task_question();
            tq.setTask_id(taskId);
            tq.setQuestion_id(question.getId());
            tqMapper.insert(tq);
        }


    }

    @Override
    public List<Task> listByCourseId(int courseId) {
        List<Task> tasks = taskMapper.listByCourseId(courseId);


        return tasks;
    }

    @Transactional
    @Override
    public void delete(int taskId) {
        List<Score> scores = scoreMapper.listByTaskId(taskId);
        for (Score score : scores)
            scoreMapper.deleteById(score.getId());
        List<Question> questions = questionMapper.findByTaskId(taskId);

        if (questions != null && !questions.isEmpty()) {
            questionMapper.deleteBatch(questions.stream().map(Question::getId).toList());
            tqMapper.deleteBatch(taskId);
        }
        taskMapper.deleteUserTaskByTaskId(taskId);

        taskMapper.deleteByTaskId(taskId);
    }

    @Override
    public void update(Task task) {
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.update(task);
    }

    @Override
    public List<Task> listByUserId(int userId) {

        List<Integer> courseIds = userMapper.getCourseIdsByUserId(userId);
        log.info("listByUserId:{}", courseIds);
        if (courseIds.isEmpty()){
            log.info("CourseIds is empty");
            List<Task> tasks = new ArrayList<>();
            return tasks;
        }
        List<Task> tasks = taskMapper.listByCourseIds(courseIds);
        return tasks;


    }

    @Override
    public Integer getTaskCountByTeacherId(int teacherId) {

        List<Integer> courseIds =taskMapper.getCourseIdsCountByTeacherId(teacherId);
        if (courseIds.isEmpty()) {
            return 0;
        }
        Integer taskCount = 0;
        for (Integer courseId : courseIds){
            taskCount += taskMapper.getTaskCountByCourseId(courseId);
        }
        return taskCount;
    }


    /**
     * 用户开始任务
     *
     * @param taskId
     * @param userId
     */
    @Transactional
    public void startTask(int taskId, int userId) {
        Optional<Task> taskOptional = taskMapper.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }

        System.out.println("User " + userId + " started task " + taskId);

        // **修正：发布用户开始任务事件，使用 'click'**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "task",
                taskId,
                "click",       // actionType: 使用 'click'
                null,
                "{\"action\":\"START_TASK\", \"description\":\"用户开始任务: " + taskId + "\"}" // detail
        ));
    }

    /**
     * 用户提交整个任务或测验。
     *
     * @param taskId            任务ID
     * @param userId            用户ID
     * @param rawScore          原始得分
     * @param submissionContent 提交内容 (可以是JSON字符串)
     * @return Score对象
     */
    @Transactional
    public Score submitTask(int taskId, int userId, BigDecimal rawScore, String submissionContent) {
        Optional<Task> taskOptional = taskMapper.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }

        Score score = new Score();
        score.setUserId(userId);
        score.setTaskId(taskId);
        score.setTotalScore(rawScore);
        List<Score> scores = new ArrayList<>();
        scores.add(score);
        scoreMapper.insertBatch(scores);
        System.out.println("User " + userId + " submitted task " + taskId + " with score " + rawScore);

        // **添加：发布用户提交任务事件，使用 'submit'**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "task",
                taskId,
                "submit",    // actionType: 使用 'submit'
                null,
                "{\"score\":" + rawScore + ", \"content\":\"" + submissionContent + "\"}" // detail
        ));

        conceptMasteryService.updateMasteryForTaskSubmission(userId, taskId);
        return score;
    }

    /**
     * 用户回答单个问题。
     *
     * @param questionId 问题ID
     * @param userId     用户ID
     * @param userAnswer 用户答案
     * @param isCorrect  是否正确
     */
    @Transactional
    public void answerQuestion(int questionId, int userId, String userAnswer, boolean isCorrect) {

        Optional<Question> questionOptional = Optional.ofNullable(questionMapper.findById(questionId));
        if (questionOptional.isEmpty()) {
            throw new IllegalArgumentException("Question not found: " + questionId);
        }

        System.out.println("User " + userId + " answered question " + questionId + ". Correct: " + isCorrect);

        // **添加：发布用户回答问题事件，使用 'answer'**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                userId,
                "question", // target_type 可以是 QUESTION
                questionId,
                "answer",   // actionType: 使用 'answer'
                null,
                "{\"userAnswer\":\"" + userAnswer + "\", \"isCorrect\":" + isCorrect + "}" // detail
        ));
    }

    @Override
    public List<QuestionDTO> getQuestionsByTaskId(int taskId) {
        List<Integer> questionIds =tqMapper.listByTaskId(taskId);

        List<Question> questions = questionMapper.findByIds(questionIds);
        List<QuestionDTO> questionDTOs = new ArrayList<>();

        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setDifficulty(question.getDifficulty().name());

            // 获取选项
            List<Option> options = optionMapper.findByQuestionId(question.getId());
            List<OptionDTO> optionDTOs = new ArrayList<>();
            for (Option option : options) {
                OptionDTO optionDTO = new OptionDTO();
                BeanUtils.copyProperties(option, optionDTO);
                optionDTOs.add(optionDTO);
            }
            questionDTO.setOptions(optionDTOs);

            questionDTOs.add(questionDTO);
        }

        return questionDTOs;
    }
    //判断对错
    @Override
    public List<TaskAnswerResultDTO> submitAnswerAndJudge(List<SubmitAnswerDTO> submitAnswerDTOList) {
        List<TaskAnswerResultDTO> results = new ArrayList<>();

        for (SubmitAnswerDTO submitAnswerDTO : submitAnswerDTOList) {
            TaskAnswerResultDTO resultDTO = new TaskAnswerResultDTO();
            BeanUtils.copyProperties(submitAnswerDTO, resultDTO); // 复制基本信息

            Optional<Question> questionOptional = Optional.ofNullable(questionMapper.findById(submitAnswerDTO.getQuestionId()));
            if (questionOptional.isEmpty()) {
                // 如果问题不存在，可以记录日志或抛出异常，这里选择跳过当前问题并记录错误
                log.warn("Question not found for submitted answer: " + submitAnswerDTO.getQuestionId());
                // 你也可以在这里设置一个错误状态或默认值到 resultDTO 中
                resultDTO.setCorrect(false); // 标记为不正确
                resultDTO.setPoint(BigDecimal.ZERO); // 得分为0
                results.add(resultDTO);
                continue; // 跳过当前问题，处理下一个
            }

            Question question = questionOptional.get();
            String correctAnswer = question.getAnswer(); // 获取正确答案
            BigDecimal point = BigDecimal.ZERO; // 默认分数为0

            // 比较用户答案和正确答案
            boolean isCorrect = submitAnswerDTO.getUserAnswer().equalsIgnoreCase(correctAnswer);
            resultDTO.setCorrect(isCorrect); // 设置是否正确
            resultDTO.setCorrectAnswer(correctAnswer); // 设置正确答案

            // 根据答案的正确性确定得分
            if (isCorrect) {
                // 这里假设 Question 实体中有一个 getScore() 方法来获取该题目的分值
                // 如果分数是存储在 task_question 表中，你可以使用 tqMapper.findMaxScoreByTaskAndQuestionId
                point = question.getPoint();
            }
            resultDTO.setPoint(point); // 设置得分

            // 发布用户回答问题的事件
            answerQuestion(submitAnswerDTO.getQuestionId(), submitAnswerDTO.getUserId(), submitAnswerDTO.getUserAnswer(), isCorrect);

            results.add(resultDTO); // 将处理结果添加到列表中
        }

        return results; // 返回结果列表
    }
}

