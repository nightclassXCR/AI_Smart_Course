package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO questions (context, difficulty,updated_at,created_at, point,course_id,answer,chapter_id)" +
            " VALUES (#{context}, #{difficulty},#{updatedAt},#{createdAt},#{point}, #{courseId},#{answer},#{chapterId})")
    int insert(Question question);

    @Select("SELECT * FROM questions WHERE id = #{id}")
    Question findById(@Param("id") int id);

    //根据课程
    @Select("SELECT * FROM questions WHERE course_id = #{courseId}")
    List<Question> findByCourseId(@Param("courseId") int courseId);

    //根据章节
    @Select("SELECT * FROM questions WHERE chapter_id = #{chapterId}")
    List<Question> findByChapterId(@Param("chapterId") int chapterId);

    @Delete("DELETE FROM questions WHERE id = #{id}")
    int delete(@Param("id") Integer id);

    @Update("UPDATE questions SET context = #{context}, difficulty = #{difficulty}," +
            " updated_at = #{updatedAt}, created_at = #{createdAt}, point = #{point}," +
            " course_id = #{courseId}, answer = #{answer}, chapter_id = #{chapterId} WHERE id = #{id}")
    int update(Question question);

    // ✅ 批量插入
    int insertBatch(@Param("questions") List<Question> questions);

    // ✅ 批量删除
    int deleteBatch(@Param("ids") List<Integer> ids);
}

