package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Course_user;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CourseUserMapper {

    /**
     * 插入一条用户课程关联记录（学生注册课程或教师分配课程）。
     */
    @Insert("INSERT INTO course_user (course_id, user_id, role) VALUES (#{courseId}, #{userId}, #{role})")
    void insertCourseUser(Course_user courseUser);

    /**
     * 删除一条用户课程关联记录（学生退课）。
     */
    @Delete("DELETE FROM course_user WHERE course_id = #{courseId} AND user_id = #{userId}")
    void deleteCourseUser(@Param("courseId") Long courseId, @Param("userId") Long userId);

    /**
     * 根据课程ID获取所有关联用户。
     */
    @Select("SELECT course_id, user_id, role FROM course_user WHERE course_id = #{courseId}")
    List<Course_user> findUsersByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据用户ID获取所有关联课程。
     */
    @Select("SELECT course_id, user_id, role FROM course_user WHERE user_id = #{userId}")
    List<Course_user> findCoursesByUserId(@Param("userId") Long userId);

    /**
     * 查找特定用户在特定课程中的角色。
     */
    @Select("SELECT course_id, user_id, role FROM course_user WHERE course_id = #{courseId} AND user_id = #{userId}")
    Optional<Course_user> findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);

    /**
     * 查找某个课程的所有学生用户ID。
     */
    @Select("SELECT user_id FROM course_user WHERE course_id = #{courseId} AND role = 'STUDENT'")
    List<Long> findStudentIdsByCourseId(@Param("courseId") Long courseId);

    /**
     * 查找某个用户作为教师所教授的所有课程ID。
     */
    @Select("SELECT course_id FROM course_user WHERE user_id = #{userId} AND role = 'TEACHER'")
    List<Long> findTeachingCourseIdsByTeacherId(@Param("userId") Long userId);
}

