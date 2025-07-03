package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;


@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO questions (content, difficulty,updated_at,created_at, point,course_id,answer,chapter_id)" +
            " VALUES (#{content}, #{difficulty},#{updatedAt},#{createdAt},#{point}, #{courseId},#{answer},#{chapterId})")
    // useGeneratedKeys 设为 true 表示使用自增主键；keyProperty 指定实体类中对应主键的属性名（这里假设 Question 类里有 id 属性）
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Question question);

    @Select("SELECT * FROM questions WHERE id = #{id}")
    Question findById(@Param("id") int id);

    //根据课程
    @Select("SELECT * FROM questions WHERE course_id = #{courseId}")
    List<Question> findByCourseId(@Param("courseId") int courseId);

    //根据章节
    @Select("SELECT * FROM questions WHERE chapter_id = #{chapterId}")
    List<Question> findByChapterId(@Param("chapterId") int chapterId);

    // ✅ 获取所有题目
    @Select("SELECT * FROM questions ORDER BY created_at DESC")
    List<Question> getAllQuestions();

    @Delete("DELETE FROM questions WHERE id = #{id}")
    int delete(@Param("id") int id);

    @Update("UPDATE questions SET content = #{content}, difficulty = #{difficulty}," +
            " updated_at = #{updatedAt}, created_at = #{createdAt}, point = #{point}," +
            " course_id = #{courseId}, answer = #{answer}, chapter_id = #{chapterId} WHERE id = #{id}")
    int update(Question question);


    // ✅ 批量插入
    int insertBatch(@Param("questions") List<Question> questions);

    // ✅ 批量删除
    int deleteBatch(@Param("ids") List<Integer> ids);

    List<Question> findByTaskId(int taskId);

    //批量查询
    List<Question> findByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据问题ID获取关联的概念信息
     */
    @MapKey("concept_id")
    List<Map<String, Object>> getConceptsByQuestionId(int questionId);

    /**
     * 根据问题ID列表批量获取概念信息映射
     */
    @MapKey("concept_id")
    List<Map<String, Object>> getConceptsByQuestionIds(@Param("questionIds") List<Integer> questionIds);

    /**
     * 为问题关联概念知识点
     */
    void linkQuestionToConcepts(@Param("questionId") int questionId, @Param("conceptIds") List<Integer> conceptIds);

    /**
     * 删除问题的所有概念关联
     */
    void deleteQuestionConceptLinks(int questionId);

    /**
     * 根据 conceptId 查询关联的题目
     * @param conceptId 知识点 ID
     * @return 题目列表
     */
    List<Question> findByConceptId(int conceptId);
}