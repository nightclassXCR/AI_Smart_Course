package com.dd.ai_smart_course.service.base;


import com.dd.ai_smart_course.dto.TaskDTO;
import com.dd.ai_smart_course.entity.Task;

import java.util.List;

public interface TaskService {

    void insert(TaskDTO taskDTO);

    List<Task> listByCourseId(int courseId);

    void delete(int taskId);

    void update(Task task);

    List<Task> listByUserId(int userId);
}
