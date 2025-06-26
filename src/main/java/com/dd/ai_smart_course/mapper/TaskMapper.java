package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Task;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {



    void insertBatch(Task task);

    List<Task> listByCourseId(int courseId);


    @Delete("DELETE FROM tasks WHERE id = #{taskId}")
    void deleteByTaskId(int taskId);

    void update(Task task);

    @Select("SELECT * FROM tasks WHERE id = #{taskId}")
    Optional<Task> findById(int taskId);
}
