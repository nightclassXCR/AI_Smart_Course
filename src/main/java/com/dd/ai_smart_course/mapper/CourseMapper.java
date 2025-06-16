package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    @Update("DELETE FROM course WHERE id = #{id}")
    int deleteCourse(int id);
}
