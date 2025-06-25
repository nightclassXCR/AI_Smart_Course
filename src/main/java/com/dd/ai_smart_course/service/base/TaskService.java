package com.dd.ai_smart_course.service.base;


import com.dd.ai_smart_course.entity.Task;

import java.util.List;

public interface TaskService {

    void insertBatch(List<Task> tasks);

    List<Task> listByCourseId(int courseId);

    void delete(int taskId);

    void update(Task task);
}
