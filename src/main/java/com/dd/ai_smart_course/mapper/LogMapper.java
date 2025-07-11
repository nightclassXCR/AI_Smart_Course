package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.dto.LearningLogDTO;
import com.dd.ai_smart_course.entity.LearningLog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LogMapper {

    String USER_ID = "user_id";
    String TARGET_TYPE = "target_type";
    String ACTION_TYPE = "action_type";
    String ACTION_TIME = "action_time";

//    @Select({" <script>"+
//            "SELECT id, user_id, target_type, target_id, action_type, action_time, duration, detail "+
//            "FROM learning_logs "+
//            "<where>"+
//            "<if test='userId != null'> AND user_id = #{userId} </if>"+
//            "<if test='targetType != null'> AND target_type = #{targetType} </if>"+
//            "<if test='actionType != null'> AND action_type = #{actionType} </if>"+
//            "<if test='startDate != null'> AND action_time >= #{startDate} </if>"+
//            "<if test='endDate != null'> AND action_time <= #{endDate} </if>"+
//            "</where>"+
//            "ORDER BY action_time DESC"+
//            "<if test='limit != null and offset != null'> LIMIT #{limit} OFFSET #{offset} </if>"+
//            "</script>"})
//    @Results(id = "learningLogResultMap", value = {
//            @Result(property = "id", column = "id", id = true),
//            @Result(property = "userId", column = USER_ID),
//            @Result(property = "targetType", column = TARGET_TYPE),
//            @Result(property = "targetId", column = "target_id"),
//            @Result(property = "actionType", column = ACTION_TYPE),
//            @Result(property = "actionTime", column = ACTION_TIME),
//            @Result(property = "duration", column = "duration"),
//            @Result(property = "detail", column = "detail")
//    })

    List<LearningLog> findLearningLogs(@Param("userId") int userId,
                                       @Param("targetType") String targetType,
                                       @Param("actionType") String actionType,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("offset") Integer offset,
                                       @Param("limit") Integer limit);

    /**
     * 根据条件统计学习日志记录总数，用于分页。
     * 使用 @Select 注解定义动态 SQL，替代原有的 @SelectProvider。
     *
     * @param userId     用户ID (可选)
     * @param targetType   目标类型 (可选)，建议使用预定义枚举值以提高安全性
     * @param actionType   动作类型 (可选)，建议使用预定义枚举值以提高安全性
     * @param startDate    开始时间 (可选)，包含时间部分，推荐使用当天 00:00:00 表示全天开始
     * @param endDate      结束时间 (可选)，包含时间部分，推荐使用当天 23:59:59 表示全天结束
     * @return 学习日志总数
     */
//    @Select({"<script>"+
//            "SELECT COUNT(id)"+
//            "FROM learning_logs"+
//            "<where>"+
//            "<if test='userId != null'> AND " + USER_ID + " = #{userId} </if>"+
//            "<if test='targetType != null'> AND " + TARGET_TYPE + " = #{targetType} </if>"+
//            "<if test='actionType != null'> AND " + ACTION_TYPE + " = #{actionType} </if>"+
//            "<if test='startDate != null'> AND " + ACTION_TIME + " >= #{startDate} </if>"+
//            "<if test='endDate != null'> AND " + ACTION_TIME + " <= #{endDate} </if>"+
//            "</where>"+
//            "</script>"})
    int countLearningLogs(@Param("userId") int userId,
                          @Param("targetType") String targetType,
                          @Param("actionType") String actionType,
                          @Param("startDate") LocalDateTime startDate,
                          @Param("endDate") LocalDateTime endDate);

    /**
     * 插入一条学习日志记录。
     * 使用 @Insert 注解定义 INSERT SQL。
     * useGeneratedKeys 和 keyProperty 用于获取数据库自动生成的主键。
     *
     * @param log 学习日志实体对象
     * @return
     */
    @Insert("INSERT INTO learning_logs (user_id, target_type, target_id, action_type, action_time, duration, detail) " +
            "VALUES (#{userId}, #{targetType}, #{targetId}, #{actionType}, #{actionTime}, #{duration}, #{detail})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id") // 对应数据库的自增ID
    int insertLearningLog(LearningLog log);

    /**
     * 根据 conceptId 获取相关的学习日志记录。
     *
     * @param conceptId 概念ID
     * @return 学习日志列表
     */
    @Select("SELECT * FROM learning_logs WHERE target_id = #{conceptId} AND target_type = 'CONCEPT'")
    List<LearningLog> getLogsByConceptId(int conceptId);

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
    int countCompletedChaptersByUserAndCourse(@Param("userId") int userId, @Param("courseId") int courseId);

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

    @Select("SELECT * FROM learning_logs")
    List<LearningLog> getAllLogs();

    @Select("SELECT * FROM learning_logs WHERE id = #{id}")
    LearningLog getLogById(int id);

    @Insert("INSERT INTO learning_logs (user_id, target_type, target_id, action_type, action_time, duration, detail) " +
            "VALUES (#{userId}, #{targetType}, #{targetId}, #{actionType}, #{actionDate}, #{duration}, #{detail})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addLog(LearningLog log);

    @Update("UPDATE learning_logs SET " +
            "user_id = #{userId}, " +
            "target_type = #{targetType}, " +
            "target_id = #{targetId}, " +
            "action_type = #{actionType}, " +
            "action_time = #{actionDate}, " +
            "duration = #{duration}, " +
            "detail = #{detail} " +
            "WHERE id = #{id}")
    int updateLog(LearningLog log);

    @Delete("DELETE FROM learning_logs WHERE id = #{id}")
    int deleteLog(int id);

    @Select("SELECT SUM(duration) FROM learning_logs WHERE user_id = #{userId}")
    Double getTotalStudyTime(int userId);

    LearningLogDTO findLatestLearningLogInCourseDTO(@Param("userId") int userId, @Param("courseId") int courseId);
}