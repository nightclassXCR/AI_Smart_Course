package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Task;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {


    void insertBatch(List<Task> tasks);

    List<Task> listByCourseId(int courseId);

    @Delete("DELETE FROM tasks WHERE id = #{taskId}")
    void deleteByTaskId(int taskId);
}
