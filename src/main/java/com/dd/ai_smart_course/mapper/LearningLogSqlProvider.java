package com.dd.ai_smart_course.mapper;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.time.LocalDateTime;


import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;



public class LearningLogSqlProvider implements ProviderMethodResolver {
    private static final String TABLE_NAME = "learning_logs";

    /**
     * 构建 findLearningLogs 方法的动态 SQL。
     *
     * @param userId     用户ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param offset     分页偏移量
     * @param limit      分页限制数量
     * @return 构建好的 SQL 字符串
     */
    public String findLearningLogsSql(Long userId, String targetType, String actionType, LocalDateTime startDate, LocalDateTime endDate, Integer offset, Integer limit) {
        if (limit != null && offset != null) {
            // 手动拼接LIMIT/OFFSET部分
            String limitOffset = String.format(" LIMIT %d OFFSET %d", limit, offset);
            String sql = new SQL() {{
                SELECT("id, user_id, target_type, target_id, action_type, action_time, duration, detail");
                FROM(TABLE_NAME);
                applyWhere(userId, targetType, actionType, startDate, endDate);
                ORDER_BY("action_time DESC");
            }}.toString();
            return sql + limitOffset;
        }
        return new SQL() {{
            SELECT("id, user_id, target_type, target_id, action_type, action_time, duration, detail");
            FROM(TABLE_NAME);
            applyWhere(userId, targetType, actionType, startDate, endDate);
            ORDER_BY("action_time DESC");
        }}.toString();
    }

    /**
     * 构建 countLearningLogs 方法的动态 SQL。
     *
     * @param userId     用户ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 构建好的 SQL 字符串
     */
    public String countLearningLogsSql(Long userId, String targetType, String actionType, LocalDateTime startDate, LocalDateTime endDate) {
        return new SQL() {{
            SELECT("COUNT(id)");
            FROM(TABLE_NAME);
            applyWhere(userId, targetType, actionType, startDate, endDate);
        }}.toString();
    }

    /**
     * 私有方法，用于构建 WHERE 子句，供 findLearningLogsSql 和 countLearningLogsSql 复用。
     *
     * @param userId     用户ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @param startDate  开始时间
     * @param endDate    结束时间
     */
    private void applyWhere(Long userId, String targetType, String actionType, LocalDateTime startDate, LocalDateTime endDate) {
        if (userId != null) {
            WHERE("user_id = #{userId}");
        }
        if (targetType != null) {
            WHERE("target_type = #{targetType}");
        }
        if (actionType != null) {
            WHERE("action_type = #{actionType}");
        }
        if (startDate != null) {
            WHERE("action_time >= #{startDate}");
        }
        if (endDate != null) {
            WHERE("action_time <= #{endDate}");
        }
    }
}
