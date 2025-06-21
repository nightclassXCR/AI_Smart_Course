package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChapterMapper {

    // 获取所有章节
    @Select("SELECT * FROM chapters")
    List<Chapter> getAllChapters();

    // 根据课程ID获取章节
    @Select("SELECT * FROM chapters WHERE course_id = #{courseId}")
    List<Chapter> getChaptersByCourseId(int courseId);

    // 添加章节
    @Insert("INSERT INTO chapters (course_id, title, content, sort_order) VALUES (#{courseId}, #{title}, #{content}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addChapter(Chapter chapter);

    // 根据课程名称获取课程ID
    @Select("SELECT id FROM course WHERE name = #{courseName}")
    Integer getCourseIdByCourseName(String courseName);

    // 根据章节ID更新章节信息
    @Update("UPDATE chapters SET course_id = #{courseId}, title = #{title}, content = #{content}, sort_order = #{sortOrder} WHERE id = #{id}")
    int updateChapter(Chapter chapter);

    //  根据章节ID删除章节
    @Delete("DELETE FROM chapters WHERE id = #{id}")
    // 注意：删除章节可能需要处理级联删除（例如：关联的知识点、学习日志等）
    // 在实际生产中，通常会使用逻辑删除或者在Service层进行事务管理和相关联数据的删除操作。
    // 这里仅删除章节本身。
    int deleteChapter(int id);

    /**
     * 更新章节的顺序 (sequence)
     */
    @Update("UPDATE chapters SET sequence = #{sequence} WHERE id = #{id}")
    int updateChapterSequence(@Param("id") Long id, @Param("sequence") int sequence);

    // --- Chapter-Concept Related Queries ---

    /**
     * 获取某章节下的所有知识点
     * 注意：这个方法与 ConceptMapper 中的 getConceptsByChapterId 功能类似，
     * 但在 ChapterService 中调用 ChapterMapper 更符合领域驱动设计中对章节边界的封装。
     */
    @Select("SELECT id, chapter_id, name, description FROM concepts WHERE chapter_id = #{chapterId}")
    List<Concept> getConceptsByChapterId(@Param("chapterId") Long chapterId);

    @Delete("DELETE FROM chapters WHERE course_id = #{courseId}")
    int deleteByCourseId(int courseId);

    @Select("SELECT * FROM chapters WHERE id = #{id}")
    Optional<Chapter> findById(@Param("id") Long chapterId);
}
