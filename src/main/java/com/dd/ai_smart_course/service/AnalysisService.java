package com.dd.ai_smart_course.service;


import com.dd.ai_smart_course.R.PaginationResult;
import com.dd.ai_smart_course.dto.ConceptMasteryDTO;
import com.dd.ai_smart_course.dto.LearningLogDTO;
import com.dd.ai_smart_course.entity.Concept_mastery;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dd.ai_smart_course.entity.LearningLog;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.dd.ai_smart_course.dto.LearningStatsDTO;
import com.dd.ai_smart_course.mapper.ScoreMapper;

@Service
public class AnalysisService {


    @Autowired
    private LogMapper learningLogMapper;
    @Autowired
    private ConceptMasteryMapper conceptMasteryMapper; // 假设已实现
    @Autowired
    private UserMapper userMapper; // 用于获取用户名
    @Autowired
    private CourseMapper courseMapper; // 用于获取课程名
    @Autowired
    private ConceptMapper conceptMapper; // 用于获取概念名
    @Autowired
    private ChapterMapper chapterMapper; // 用于获取章节数
    @Autowired
    private ScoreMapper scoreMapper;

    /**
     * 获取学习日志。
     *
     * @param userId     用户ID (可选)
     * @param targetType 目标类型 (可选)
     * @param actionType 动作类型 (可选)
     * @param startDate  开始时间 (可选)
     * @param endDate    结束时间 (可选)
     * @param page       页码 (从0开始)
     * @param size       每页大小
     * @return 学习日志分页结果
     */
    @Transactional(readOnly = true) // 只读事务，提高性能
    public PaginationResult<LearningLogDTO> getLearningLogs(int userId, String targetType, String actionType,
                                                            LocalDateTime startDate, LocalDateTime endDate,
                                                            int page, int size) {
        int offset = page * size;
        List<LearningLog> logs = learningLogMapper.findLearningLogs(userId, targetType, actionType, startDate, endDate, offset, size);
        long totalElements = learningLogMapper.countLearningLogs(userId, targetType, actionType, startDate, endDate);

        List<LearningLogDTO> dtos = logs.stream().map(log -> {
            LearningLogDTO dto = new LearningLogDTO();
            BeanUtils.copyProperties(log, dto); // 复制相同名称的属性

            // 补充用户名
            User user = userMapper.getUserById((int) log.getUserId());
            if (user != null) {
                dto.setUsername(user.getUsername());
            }

            return dto;
        }).collect(Collectors.toList());

        return new PaginationResult<>(dtos, totalElements, page, size);
    }

    /**
     * 获取概念掌握度数据。
     *
     * @param userId    用户ID (可选)
     * @param conceptId 概念ID (可选)
     * @param courseId  课程ID (可选，用于查询课程下所有概念掌握度)
     * @return 概念掌握度列表
     */
    @Transactional(readOnly = true)
    public List<ConceptMasteryDTO> getConceptMastery(int userId, int conceptId, int courseId) {
        List<Concept_mastery> masteries;
        if (userId != 0 && conceptId != 0) {
            masteries = conceptMasteryMapper.findByUserIdAndConceptId(userId, conceptId).map(List::of).orElse(List.of());
        } else if (userId != 0) {
            masteries = conceptMasteryMapper.findMasteriesByUserId(userId);
        } else if (conceptId != 0) {
            masteries = conceptMasteryMapper.findAllMasteries().stream()
                    .filter(cm -> cm.getConceptId()==conceptId)
                    .collect(Collectors.toList());
        } else if (courseId != 0) {
            List<Integer> conceptIdsInCourse = conceptMapper.findConceptIdsByCourseId(courseId); // 假设 ConceptMapper 有此方法
            masteries = conceptMasteryMapper.findAllMasteries().stream()
                    .filter(cm -> conceptIdsInCourse.contains(cm.getConceptId()))
                    .collect(Collectors.toList());
        } else {
            masteries = conceptMasteryMapper.findAllMasteries();
        }

        return masteries.stream().map(mastery -> {
            ConceptMasteryDTO dto = new ConceptMasteryDTO();
            BeanUtils.copyProperties(mastery, dto);

            // 补充用户名和概念名
            User user = userMapper.getUserById(mastery.getUserId());
            if (user != null) {
                dto.setUsername(user.getUsername());
            }
            conceptMapper.findById(mastery.getConceptId()).ifPresent(concept -> dto.setConceptName(concept.getName()));

            return dto;
        }).collect(Collectors.toList());
    }

//    /**
//     * 获取用户在特定课程的学习进度。
//     *
//     * @param userId   用户ID
//     * @param courseId 课程ID
//     * @return 用户课程进度DTO
//     */
//    @Transactional(readOnly = true)
//    public UserCourseDTO getUserCourseProgress(Long userId, Long courseId) {
//        UserCourseDTO dto = new UserCourseDTO();
//
//        // 1. 获取用户信息
//        User user = userMapper.getUserById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
//        dto.setUserId(user.getId());
//        dto.setUsername(user.getUsername());
//
//        // 2. 获取课程信息
//        Course course = courseMapper.findById(courseId)
//                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
//        dto.setCourseId(course.getId());
//        dto.setCourseName(course.getName());
//
//        // 3. 计算已完成章节数
//        int completedChapters = learningLogMapper.countCompletedChaptersByUserAndCourse(userId, courseId);
//        dto.setCompletedChapters(completedChapters);
//
//        // 4. 获取课程总章节数
//        int totalChapters = chapterMapper.countChaptersInCourse(courseId);
//        dto.setTotalChapters(totalChapters);
//
//        // 5. 计算完成百分比
//        double completionPercentage = (totalChapters > 0) ? ((double) completedChapters / totalChapters * 100.0) : 0.0;
//        dto.setCompletionPercentage(completionPercentage);
//
//        return dto;
//    }

    /**
     * 获取用户的学习统计数据。
     *
     * @param userId 用户ID
     * @param startDate 开始时间（可选）
     * @param endDate 结束时间（可选）
     * @return LearningStatsDto
     */
    @Transactional(readOnly = true)
    public LearningStatsDTO getLearningStats(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        LearningStatsDTO stats = new LearningStatsDTO();
        stats.setUserId(userId);
        // 用户名
        User user = userMapper.getUserById(userId);
        if (user != null) {
            stats.setUsername(user.getUsername());
        }
        // 获取所有日志（可加时间范围）
        List<LearningLog> logs = learningLogMapper.findLearningLogs(userId, null, null, startDate, endDate, null, null);
        // 总学习时长（秒）
        long totalStudyTime = logs.stream().mapToLong(l -> l.getDuration() == 0 ? 0 : l.getDuration()).sum();
        stats.setTotalStudyTime(totalStudyTime);
        // 总操作次数
        stats.setTotalActions(logs.size());
        // 完成章节数（targetType=CHAPTER, actionType=COMPLETE）
        long completedChapters = logs.stream().filter(l -> "CHAPTER".equalsIgnoreCase(l.getTargetType()) && "COMPLETE".equalsIgnoreCase(l.getActionType())).map(LearningLog::getTargetId).distinct().count();
        stats.setCompletedChapters((int) completedChapters);
        // 完成概念数（统计 concept_mastery 表中 mastery_level >= 60 的数量）
        List<com.dd.ai_smart_course.entity.Concept_mastery> masteries = conceptMasteryMapper.findMasteriesByUserId(userId);
        long completedConcepts = masteries.stream()
            .filter(m -> {
                Double level = m.getMasteryLevel();
                return level != null && level >= 60.0;
            })
            .count();
        stats.setCompletedConcepts((int) completedConcepts);
        // 平均分数（取 scores 表中 finalScore 平均值）
        double averageScore = 0.0;
        try {
            List<com.dd.ai_smart_course.entity.Score> scores = scoreMapper.getScoreByUserId(userId);
            if (scores != null && !scores.isEmpty()) {
                averageScore = scores.stream().filter(s -> s.getFinalScore() != null).mapToDouble(s -> s.getFinalScore().doubleValue()).average().orElse(0.0);
            }
        } catch (Exception e) {
            // ignore
        }
        stats.setAverageScore(averageScore);
        // 最后活动时间
        LocalDateTime lastActivityTime = logs.stream().map(l -> l.getActionTime() != null ? l.getActionTime().toLocalDateTime() : null).filter(t -> t != null).max(LocalDateTime::compareTo).orElse(null);
        stats.setLastActivityTime(lastActivityTime);
        // 活跃天数（按 actionTime 的日期去重计数）
        long activeDays = logs.stream().map(l -> l.getActionTime() != null ? l.getActionTime().toLocalDateTime().toLocalDate() : null).filter(d -> d != null).distinct().count();
        stats.setActiveDays((int) activeDays);
        return stats;
    }


    /**
     * 统计该用户的总学习时间
     */
    public Double getTotalStudyTime(int userId) {
        Double sum = learningLogMapper.getTotalStudyTime(userId);
        sum = sum / 3600;
        return sum;
    }
}
