package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 获取所有课程
    @Select("SELECT * FROM course")
    List<Course> getAllCourses();

    // 获取课程详情
    @Select("SELECT * FROM course WHERE id = #{id}")
    Course getCourseById(int id);

    // 添加课程
    @Insert("INSERT INTO course (name, teacher_id, description, created_at) VALUES (#{name}, #{teacherId}, #{description}, #{createdAt})")
    int addCourse(Course course);

    // 更新课程信息
    @Update("UPDATE course SET name = #{name}, teacher_id = #{teacherId}, description = #{description}, created_at = #{createdAt} WHERE id = #{id}")
    int updateCourse(Course course);

    // 删除课程
    // Todo: 添加逻辑删除功能
    @Delete("DELETE FROM courses WHERE id = #{id}")
    // 注意：删除课程可能需要处理级联删除（例如：相关章节、知识点、用户课程关联、学习日志、成绩等）
    // 在实际生产中，通常会使用逻辑删除（软删除）或者在Service层进行事务管理和相关联数据的删除操作。
    // 这里仅删除课程本身。
    int deleteCourse(int id);

    @Select("SELECT id, name, teacher_id, description, status FROM courses WHERE teacher_id = #{teacherId}")
    List<Course> getCoursesByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 获取课程下所有知识点 (通过 chapters 表关联)
     */
    @Select("SELECT c.id, c.chapter_id, c.name, c.description " + // 假设 Concept 实体有 description
            "FROM concepts c " +
            "JOIN chapters chap ON c.chapter_id = chap.id " +
            "WHERE chap.course_id = #{courseId}")
    List<Concept> getConceptsByCourse(@Param("courseId") Long courseId);

    /**
     * 【辅助方法】用于 CourseService 中按章节分组知识点时获取所有章节的知识点，
     * 包括那些可能没有知识点的章节 (LEFT JOIN)
     * 这里返回的是原始的 Concept 列表，Service 层进行分组
     */
    @Select("SELECT " +
            "    c.id AS concept_id, c.chapter_id, c.name AS concept_name, c.description AS concept_description, " +
            "    chap.id AS chapter_id_fk, chap.course_id, chap.title AS chapter_title, chap.sequence " +
            "FROM concepts c " +
            "RIGHT JOIN chapters chap ON c.chapter_id = chap.id " + // RIGHT JOIN 确保所有章节都被选中
            "WHERE chap.course_id = #{courseId} " +
            "ORDER BY chap.sequence ASC, c.id ASC") // 确保章节和知识点都有序
    @Results({
            @Result(property = "id", column = "concept_id"),
            @Result(property = "name", column = "concept_name"),
            @Result(property = "description", column = "concept_description"),
            @Result(property = "chapterId", column = "chapter_id")
            // Concept 实体中没有 chapter_id_fk, course_id, chapter_title, chapter_sequence这些字段
            // 这些字段是辅助用于Service层进行分组的，不会直接映射到Concept实体
    })
    List<Concept> getConceptsWithChapterInfoByCourse(@Param("courseId") Long courseId);

    // 获取所有章节的详细信息，用于分组。
    // 这个方法可以复用 getChaptersByCourse，或者专门定义一个。
    // 为了 getConceptsGroupedByChapter 的方便，这里直接获取所有相关章节
    @Select("SELECT id, course_id, title, sequence FROM chapters WHERE course_id = #{courseId} ORDER BY sequence ASC")
    List<Chapter> findChaptersForGrouping(@Param("courseId") Long courseId);

    @Delete("DELETE FROM concepts WHERE course_id = #{courseId}")
    int deleteByCourseId(int courseId);
}
