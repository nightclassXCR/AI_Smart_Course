package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.QA;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QAMapper {

    // 获取所有QA记录
    @Select("SELECT * FROM qa-records")
    List<QA> getAllQAs();

    // 获取QA记录详情
    @Select("SELECT * FROM qa-records WHERE id = #{id}")
    QA getQAById(int id);

    // 添加QA记录
    @Insert("INSERT INTO qa-records (user_id, course_id, concept_id, responder_id, question_text, answer_text, responder_type, created_at, answered_at, status, rating)" +
            "VALUES (#{userId}, #{courseId}, #{conceptId}, #{responderId}, #{questionText}, #{answerText}, #{responderType}, #{createdAt}, #{answeredAt}, #{status}, #{rating})")
    int addQA(QA qa);

    // 更新QA记录信息
    @Update("UPDATE qa-records SET user_id = #{userId}, course_id = #{courseId}, concept_id = #{conceptId}, responder_id = #{responderId}, question_text = #{questionText}," +
            "answer_text = #{answerText}, responder_type = #{responderType}, created_at = #{createdAt}, status = #{status}, rating = #{rating} WHERE id = #{id}")
    int updateQA(QA qa);

    // 删除QA记录
    @Delete("DELETE FROM qa-records WHERE id = #{id}")
    int deleteQA(int id);

    @Delete("DELETE FROM qa-records WHERE course_id = #{id}")
    int deleteByCourseId(int id);
}