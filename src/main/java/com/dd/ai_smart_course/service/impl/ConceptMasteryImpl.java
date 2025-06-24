package com.dd.ai_smart_course.service.impl;


import com.dd.ai_smart_course.entity.Concept_mastery;
import com.dd.ai_smart_course.mapper.*;
import com.dd.ai_smart_course.service.ConceptMasteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConceptMasteryImpl implements ConceptMasteryService {

    @Autowired
    private ConceptMasteryMapper conceptMasteryMapper;
    @Autowired
    private LogMapper learnLogMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private ConceptQuestionMapper conceptQuestionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ConceptMapper conceptMapper;

    /**
     * 定时任务：每天凌晨2点重新计算所有用户的概念掌握度
     * 生产环境可能需要更复杂的分布式锁机制确保单次执行,本次只是测试环境
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    @Transactional
    @Override
    public void recalculateAllMasteryLevels() {
        System.out.println("Starting recalculation of concept mastery levels at " + LocalDateTime.now());

        List<Long> allUserIds = userMapper.findAllUserIds(); // 假设 UserMapper 有 findAllUserIds 方法
        List<Long> allConceptIds = conceptMapper.findAllConceptIds(); // 假设 ConceptMapper 有 findAllConceptIds 方法

        for (Long userId : allUserIds) {
            for (Long conceptId : allConceptIds) {
                try {
                    int newMasteryLevel = calculateMasteryForUserConcept(userId, conceptId);

                    Optional<Concept_mastery> existingMastery = conceptMasteryMapper.findByUserIdAndConceptId(userId, conceptId);
                    if (existingMastery.isPresent()) {
                        Concept_mastery mastery = existingMastery.get();
                        mastery.setMastery_level(newMasteryLevel);
                        mastery.setLast_practiced(LocalDateTime.now());
                        conceptMasteryMapper.updateConceptMastery(mastery);
                    } else {
                        Concept_mastery mastery = new Concept_mastery(userId, conceptId, newMasteryLevel, LocalDateTime.now());
                        conceptMasteryMapper.insertConceptMastery(mastery);
                    }
                } catch (Exception e) {
                    System.err.println("Error calculating mastery for user " + userId + ", concept " + conceptId + ": " + e.getMessage());
                    // 记录错误，可能需要更详细的日志或重试机制
                }
            }
        }
        System.out.println("Finished recalculation of concept mastery levels at " + LocalDateTime.now());
    }

    /**
     * 计算单个用户对单个概念的掌握度。
     * 这是掌握度计算的核心算法，需要根据具体业务需求细化。
     *Todo: 需要根据具体业务需求进行修改
     * @param userId 用户ID
     * @param conceptId 概念ID
     * @return 计算出的掌握度分数 (0-100)
     */
    @Override
    public int calculateMasteryForUserConcept(Long userId, Long conceptId) {
        // **这里是复杂的计算逻辑，需要大量的查询和业务规则**

        // 1. 获取与概念相关的问题的正确率
        // 假设有一个方法可以查询用户在某个概念下所有关联问题的平均正确率
        // double questionAccuracy = scoreMapper.getAverageCorrectnessForConcept(userId, conceptId);
        // 简化示例：
        double questionAccuracy = Math.random(); // 0.0 - 1.0

        // 2. 获取用户在与概念相关的任务中的平均得分
        // 假设可以查询用户在某个概念下所有关联任务的平均得分
        // double taskAverageScore = scoreMapper.getAverageScoreForConceptRelatedTasks(userId, conceptId);
        // 简化示例：
        double taskAverageScore = Math.random() * 100; // 0 - 100

        // 3. 获取用户在概念相关学习资源上的总学习时长
        // 假设 LearningLogMapper 可以查询用户在特定概念上的总学习时长
        // Long totalStudyDuration = learningLogMapper.sumDurationByTarget(userId, "CONCEPT", conceptId);
        // 简化示例：
        Long totalStudyDuration = (long)(Math.random() * 3600); // 0-3600 秒

        // 4. 获取用户访问该概念的次数
        // int viewCount = learningLogMapper.countActionsByTarget(userId, "CONCEPT", conceptId, "VIEW");
        // 简化示例：
        int viewCount = (int)(Math.random() * 10);

        // 5. 综合加权计算 (示例算法，实际需要精调)
        // 赋予不同指标不同的权重
        double weightAccuracy = 0.5;
        double weightTaskScore = 0.3;
        double weightDuration = 0.1;
        double weightViewCount = 0.1;

        double scoreFromAccuracy = questionAccuracy * 100 * weightAccuracy;
        double scoreFromTask = taskAverageScore * weightTaskScore;
        double scoreFromDuration = Math.min(totalStudyDuration / 60.0, 60.0) * (weightDuration * 100 / 60.0); // 将时长转换为0-100分
        double scoreFromViewCount = Math.min(viewCount, 5) * (weightViewCount * 100 / 5.0); // 将次数转换为0-100分

        int finalMastery = (int) Math.round(scoreFromAccuracy + scoreFromTask + scoreFromDuration + scoreFromViewCount);

        // 确保掌握度在0-100之间
        return Math.max(0, Math.min(100, finalMastery));
    }

    // 可以添加一个方法在用户提交任务后，只计算与该任务相关的概念掌握度
    @Override
    @Transactional
    public void updateMasteryForTaskSubmission(Long userId, Long taskId) {
        // 1. 获取任务关联的所有概念ID
        List<Long> conceptIds = conceptQuestionMapper.findConceptIdsByTaskId(taskId); // 假设有此方法
        // 2. 遍历这些概念，为每个概念重新计算掌握度
        for (Long conceptId : conceptIds) {
            int newMasteryLevel = calculateMasteryForUserConcept(userId, conceptId);
            Optional<Concept_mastery> existingMastery = conceptMasteryMapper.findByUserIdAndConceptId(userId, conceptId);
            if (existingMastery.isPresent()) {
                Concept_mastery mastery = existingMastery.get();
                mastery.setMastery_level(newMasteryLevel);
                mastery.setLast_practiced(LocalDateTime.now());
                conceptMasteryMapper.updateConceptMastery(mastery);
            } else {
                Concept_mastery mastery = new Concept_mastery(userId, conceptId, newMasteryLevel, LocalDateTime.now());
                conceptMasteryMapper.insertConceptMastery(mastery);
            }
        }
    }


}
