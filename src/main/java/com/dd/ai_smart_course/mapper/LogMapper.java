package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.LearningLog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LogMapper {

    // 获取所有日志记录
    @Select("SELECT * FROM learning_logs")
    List<LearningLog> getAllLogs();

    // 获取日志记录详情
    @Select("SELECT * FROM learning_logs WHERE id = #{id}")
    LearningLog getLogById(int id);

    // 添加日志记录
    @Insert("INSERT INTO learning_logs (user_id, target_type, target_id, action_type, action_time, duration, detail)" +
            "VALUES (#{userId}, #{targetType}, #{targetId}, #{actionType}, #{actionTime}, #{duration}, #{detail})")
    int addLog(LearningLog learnlog);

    // 更新日志记录信息
    @Update("UPDATE learning_logs SET user_id = #{userId}, target_type = #{targetType}, target_id = #{targetId}," +
            "action_type = #{actionType}, action_time = #{actionTime}, duration = #{duration}, detail = #{detail} WHERE id = #{id}")
    int updateLog(LearningLog learnlog);

    // 删除日志记录
    @Delete("DELETE FROM learning_logs WHERE id = #{id}")
    int deleteLog(int id);

    /**
     * 插入一条学习日志记录。
     * 使用 @Insert 注解定义 INSERT SQL。
     * useGeneratedKeys 和 keyProperty 用于获取数据库自动生成的主键。
     *
     * @param log 学习日志实体对象
     */
    @Insert("INSERT INTO learning_logs (user_id, target_type, target_id, action_type, action_time, duration, detail) " +
            "VALUES (#{userId}, #{targetType}, #{targetId}, #{actionType}, #{actionTime}, #{duration}, #{detail})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id") // 对应数据库的自增ID
    void insertLearningLog(LearningLog log);

    /**
     * 根据条件查询学习日志记录。
     * 使用 @SelectProvider 结合 SqlProvider 类来构建动态 SQL，以处理复杂的条件查询。
     * @ResultMap 注解用于引用在 Mapper 接口内部定义的 @Results 映射。
     *
     * @param userId     用户ID (可选)
     * @param targetType 目标类型 (可选)
     * @param actionType 动作类型 (可选)
     * @param startDate  开始时间 (可选)
     * @param endDate    结束时间 (可选)
     * @param offset     分页偏移量 (可选)
     * @param limit      分页限制数量 (可选)
     * @return 学习日志列表
     */
    @SelectProvider(type = LearningLogSqlProvider.class, method = "findLearningLogsSql")
    @Results(id = "learningLogResultMap", value = { // 定义一个结果集映射，供多个查询复用
            @Result(property = "id", column = "id", id = true),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "targetType", column = "target_type"),
            @Result(property = "targetId", column = "target_id"),
            @Result(property = "actionType", column = "action_type"),
            @Result(property = "actionTime", column = "action_time"),
            @Result(property = "duration", column = "duration"),
            @Result(property = "detail", column = "detail")
    })
    List<LearningLog> findLearningLogs(@Param("userId") Long userId,
                                       @Param("targetType") String targetType,
                                       @Param("actionType") String actionType,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("offset") Integer offset,
                                       @Param("limit") Integer limit);

    /**
     * 根据条件统计学习日志记录总数，用于分页。
     *
     * @param userId     用户ID (可选)
     * @param targetType 目标类型 (可选)
     * @param actionType 动作类型 (可选)
     * @param startDate  开始时间 (可选)
     * @param endDate    结束时间 (可选)
     * @return 学习日志总数
     */
    @SelectProvider(type = LearningLogSqlProvider.class, method = "countLearningLogsSql")
    int countLearningLogs(@Param("userId") Long userId,
                          @Param("targetType") String targetType,
                          @Param("actionType") String actionType,
                          @Param("startDate") LocalDateTime startDate,
                          @Param("endDate") LocalDateTime endDate);

    /**
     * 统计用户在特定课程中完成的章节数量。
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 完成章节数量
     */
    @Select("SELECT COUNT(DISTINCT ll.target_id) " +
            "FROM learning_logs ll " +
            "JOIN chapters c ON ll.target_id = c.id " +
            "WHERE ll.user_id = #{userId} " +
            "AND ll.target_type = 'CHAPTER' " +
            "AND ll.action_type = 'COMPLETE' " +
            "AND c.course_id = #{courseId}")
    int countCompletedChaptersByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 计算用户在特定目标类型和目标ID上的总学习时长。
     *
     * @param userId     用户ID
     * @param targetType 目标类型 (e.g., 'RESOURCE', 'CONCEPT')
     * @param targetId   目标ID
     * @return 总时长 (秒)
     */
    @Select("SELECT COALESCE(SUM(duration), 0) " +
            "FROM learning_logs " +
            "WHERE user_id = #{userId} " +
            "AND target_type = #{targetType} " +
            "AND target_id = #{targetId} " +
            "AND action_type = 'VIEW' " + // 通常VIEW或START动作才算duration
            "AND duration IS NOT NULL")
    Long sumDurationByTarget(@Param("userId") Long userId,
                             @Param("targetType") String targetType,
                             @Param("targetId") Long targetId);

    /**
     * 计算用户在特定目标上某个动作的执行次数。
     *
     * @param userId     用户ID
     * @param targetType 目标类型
     * @param targetId   目标ID
     * @param actionType 动作类型
     * @return 动作执行次数
     */
    @Select("SELECT COUNT(id) FROM learning_logs " +
            "WHERE user_id = #{userId} " +
            "AND target_type = #{targetType} " +
            "AND target_id = #{targetId} " +
            "AND action_type = #{actionType}")
    int countActionsByTarget(@Param("userId") Long userId,
                             @Param("targetType") String targetType,
                             @Param("targetId") Long targetId,
                             @Param("actionType") String actionType);

}