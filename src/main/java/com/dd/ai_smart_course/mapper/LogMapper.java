package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Log;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LogMapper {

    // 获取所有日志记录
    @Select("SELECT * FROM learning_logs")
    List<Log> getAllLogs();

    // 获取日志记录详情
    @Select("SELECT * FROM learning_logs WHERE id = #{id}")
    Log getLogById(int id);

    // 添加日志记录
    @Insert("INSERT INTO learning_logs (user_id, target_type, target_id, action_type, action_time, duration, detail)" +
            "VALUES (#{userId}, #{targetType}, #{targetId}, #{actionType}, #{actionTime}, #{duration}, #{detail})")
    int addLog(Log log);

    // 更新日志记录信息
    @Update("UPDATE learning_logs SET user_id = #{userId}, target_type = #{targetType}, target_id = #{targetId}," +
            "action_type = #{actionType}, action_time = #{actionTime}, duration = #{duration}, detail = #{detail} WHERE id = #{id}")
    int updateLog(Log log);

    // 删除日志记录
    @Delete("DELETE FROM learning_logs WHERE id = #{id}")
    int deleteLog(int id);
}