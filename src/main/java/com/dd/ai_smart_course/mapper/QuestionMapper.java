package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    //拉取所有数据
    @Select("SELECT * FROM questions")
    List<Question> getAllQuestions();

    @Select("SELECT * FROM questions WHERE id = #{id}")
    Question getQuestionById(int id);

    //插入问题
    @Insert("INSERT INTO questions(context,type,diffculty,created_at,updated_at) VALUES (#{context}, #{type}, #{difficulty}, #{created_at}, #{updated_at})")
    int addQuestion(Question question);

    //根据ID删除问题
    @Delete("DELETE FROM questions WHERE id = #{id}")
    int deleteQuestion(int id);

    //根据ID更新问题
    @Update("UPDATE questions SET context = #{context}, type = #{type}, difficulty = #{difficulty}, created_at = #{created_at}, updated_at = #{updated_at} WHERE id = #{id}")
    int updateQuestion(Question question);
}
