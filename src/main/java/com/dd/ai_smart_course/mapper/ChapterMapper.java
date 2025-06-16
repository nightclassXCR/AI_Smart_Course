package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Chapter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChapterMapper {

    // 获取所有章节
    @Select("SELECT * FROM chapter")
    List<Chapter> getAllChapters();

    // 根据课程ID获取章节
    @Select("SELECT * FROM chapter WHERE course_id = #{courseId}")
    List<Chapter> getChaptersByCourseId(int courseId);

    // 添加章节
    @Insert("INSERT INTO chapter (course_id, title, content, sort_order) VALUES (#{courseId}, #{title}, #{content}, #{sortOrder})")
    int addChapter(Chapter chapter);

    // 根据课程名称获取课程ID
    @Select("SELECT id FROM course WHERE name = #{courseName}")
    Integer getCourseIdByCourseName(String courseName);

    // 根据章节ID更新章节信息
    @Update("UPDATE chapter SET course_id = #{courseId}, title = #{title}, content = #{content}, sort_order = #{sortOrder} WHERE id = #{id}")
    int updateChapter(Chapter chapter);


}
