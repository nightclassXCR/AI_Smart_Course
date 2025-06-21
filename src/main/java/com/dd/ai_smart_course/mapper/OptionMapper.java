package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Option;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OptionMapper {

    @Insert("INSERT INTO options (question_id, opt_key, opt_value) VALUES (#{questionId}, #{optKey}, #{optValue})")
    int insert(Option option);

    @Select("SELECT * FROM options WHERE question_id = #{questionId}")
    List<Option> findByQuestionId(@Param("questionId") int questionId);

    @Delete("DELETE FROM options WHERE question_id = #{questionId}")
    int deleteByQuestionId(@Param("questionId") int questionId);

    // ✅ 批量插入
    int insertBatch(@Param("options") List<Option> options);

    // ✅ 批量删除
    int deleteByQuestionIds(@Param("questionIds") List<Integer> questionIds);
}
