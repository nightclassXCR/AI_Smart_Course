package com.dd.ai_smart_course.service.base;


import com.dd.ai_smart_course.dto.TaskDTO;
import com.dd.ai_smart_course.entity.Task;

import java.util.List;

public interface TaskService {

    void insertBatch(TaskDTO taskDTO);

    List<Task> listByCourseId(int courseId);

    void delete(int taskId);

    void update(Task task);
}
