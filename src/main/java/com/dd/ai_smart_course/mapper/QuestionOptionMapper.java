package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.QuestionOption;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionOptionMapper {

    //根据选择题的问题ID获取说有选项
    @Select("SELECT * FROM question_options WHERE question_id = #{questionId}")
    List<QuestionOption> getOptionsByQuestionId(int questionId);

    @Delete("DELETE FROM question_options WHERE question_id = #{questionId}")
    int deleteOptionsByQuestionId(int questionId);

    @Insert("INSERT INTO question_options(question_id, opt_key, opt_value) " +
            "VALUES(#{question_id}, #{opt_Key}, #{opt_Value})")
    int insertOption(QuestionOption option);
}
