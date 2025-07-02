package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Task;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {



    void insert(Task task);

    List<Task> listByCourseId(int courseId);
    List<Task> listByCourseIds(List<Integer> courseIds);



    @Delete("DELETE FROM tasks WHERE id = #{taskId}")
    void deleteByTaskId(int taskId);

    void update(Task task);

    @Select("SELECT * FROM tasks WHERE id = #{taskId}")
    Optional<Task> findById(int taskId);

    @Select("SELECT * FROM tasks WHERE course_id = #{courseId}")
    List<Task> listByUserId(int userId);

    void insertUserTask(List<Integer> userIds, int taskId);

    @Delete("DELETE FROM user_task WHERE task_id = #{taskId}")
    void deleteUserTaskByTaskId(int taskId);

    @Select("SELECT COUNT(*) FROM tasks WHERE course_id = #{courseId}")
    Integer getTaskCountByCourseId(int teacherId);

    @Select("SELECT id from courses where teacher_id=#{teacherId}")
    List<Integer> getCourseIdsCountByTeacherId(int teacherId);
}
