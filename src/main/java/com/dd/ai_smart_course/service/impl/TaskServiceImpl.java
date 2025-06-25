package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.entity.Score;
import com.dd.ai_smart_course.entity.Task;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.QuestionMapper;
import com.dd.ai_smart_course.mapper.ScoreMapper;
import com.dd.ai_smart_course.mapper.TaskMapper;
import com.dd.ai_smart_course.service.base.ConceptMasteryService;
import com.dd.ai_smart_course.service.base.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    ConceptMasteryService conceptMasteryService;

    @Autowired
    private QuestionMapper questionMapper;


    @Override
    public void insertBatch(List<Task> tasks) {
            taskMapper.insertBatch(tasks);
    }

    @Override
    public List<Task> listByCourseId(int courseId) {
        List<Task> tasks =taskMapper.listByCourseId(courseId);


        return tasks;
    }

    @Transactional
    @Override
    public void delete(int taskId) {
        List<Score> scores = scoreMapper.listByTaskId(taskId);
        for(Score score:scores)
            scoreMapper.deleteById(score.getId());
        List<Question> questions = questionMapper.findByTaskId(taskId);
        questionMapper.deleteBatch(questions.stream().map(Question::getId).toList());

        taskMapper.deleteByTaskId(taskId);
    }

    @Override
    public void update(Task task) {
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.update(task);
    }


    /**
     *  用户开始任务
     * @param taskId
     * @param userId
     */
    @Transactional
    public void startTask(Long taskId, Long userId) {
        Optional<Task> taskOptional = taskMapper.findById(Integer.parseInt(String.valueOf(taskId)));
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }

        System.out.println("User " + userId + " started task " + taskId);

        // **修正：发布用户开始任务事件，使用 'click'**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                Math.toIntExact(userId),
                "task",
                taskId,
                "click",       // actionType: 使用 'click'
                null,
                "{\"action\":\"START_TASK\", \"description\":\"用户开始任务: " + taskId + "\"}" // detail
        ));
    }

    /**
     * 用户提交整个任务或测验。
     * @param taskId 任务ID
     * @param userId 用户ID
     * @param rawScore 原始得分
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
        List<Score> scores=new ArrayList<>();
        scores.add(score);
        scoreMapper.insertBatch(scores);
        System.out.println("User " + userId + " submitted task " + taskId + " with score " + rawScore);

        // **添加：发布用户提交任务事件，使用 'submit'**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                Math.toIntExact(userId),
                "task",
                Long.parseLong(String.valueOf(taskId)),
                "submit",    // actionType: 使用 'submit'
                null,
                "{\"score\":" + rawScore + ", \"content\":\"" + submissionContent + "\"}" // detail
        ));

        conceptMasteryService.updateMasteryForTaskSubmission(Long.parseLong(String.valueOf(userId)), Long.parseLong(String.valueOf(taskId)));

        return score;
    }

    /**
     * 用户回答单个问题。
     *
     * @param questionId 问题ID
     * @param userId 用户ID
     * @param userAnswer 用户答案
     * @param isCorrect 是否正确
     */
    @Transactional
    public void answerQuestion(Long questionId, Long userId, String userAnswer, boolean isCorrect) {

        Optional<Question> questionOptional = Optional.ofNullable(questionMapper.findById(Integer.parseInt(String.valueOf(questionId))));
        if (questionOptional.isEmpty()) {
            throw new IllegalArgumentException("Question not found: " + questionId);
        }

        System.out.println("User " + userId + " answered question " + questionId + ". Correct: " + isCorrect);

        // **添加：发布用户回答问题事件，使用 'answer'**
        eventPublisher.publishEvent(new LearningActionEvent(
                this,
                Math.toIntExact(userId),
                "QUESTION", // target_type 可以是 QUESTION
                questionId,
                "answer",   // actionType: 使用 'answer'
                null,
                "{\"userAnswer\":\"" + userAnswer + "\", \"isCorrect\":" + isCorrect + "}" // detail
        ));
    }
}
