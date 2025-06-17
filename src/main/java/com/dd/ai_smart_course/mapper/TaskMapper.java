package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO task (id,courseId,title,type,deadline,createdAt)" +
            "VALUES (#{id},#{courseId},#{title},#{type},#{type},#{deadline},#{createdAt})")
    int addTask(Task task);

    @Delete("DELETE FROM task WHERE id = #{id}")
    int deleteTask(int id);

    @Update("UPDATE task SET id = #{id},courseId = #{courseId},title = #{title},type = #{type},deadline = #{deadline}")
    int updateTask(Task task);

    @Select("SELECT * FROM task")
    List<Task> getAllTasks();

}
