package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Option;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OptionMapper {

    // 单条插入（注解方式）
    @Insert("INSERT INTO options (question_id, opt_key, opt_value) " +
            "VALUES (#{question_id}, #{optKey}, #{optValue})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 自动回填主键
    int insert(Option option);

    // 根据题目ID查询（注解方式）
    @Select("SELECT * FROM options WHERE question_id = #{questionId}")
    List<Option> findByQuestionId(@Param("questionId") int questionId);

    // 根据题目ID删除（注解方式）
    @Delete("DELETE FROM options WHERE question_id = #{questionId}")
    int deleteByQuestionId(@Param("questionId") int questionId);


    // ✅ 批量插入
    int insertBatch(@Param("options") List<Option> options);

    // ✅ 批量删除
    int deleteByQuestionIds(@Param("questionIds") List<Integer> questionIds);
}
