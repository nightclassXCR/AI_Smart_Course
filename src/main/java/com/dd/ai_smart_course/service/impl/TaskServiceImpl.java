package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Score;
import com.dd.ai_smart_course.entity.Task;
import com.dd.ai_smart_course.mapper.ScoreMapper;
import com.dd.ai_smart_course.mapper.TaskMapper;
import com.dd.ai_smart_course.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public void insertBatch(List<Task> tasks) {
            taskMapper.insertBatch(tasks);
    }

    @Override
    public List<Task> listByCourseId(int courseId) {
        List<Task> tasks =taskMapper.listByCourseId(courseId);


        return tasks;
    }

    @Override
    public void delete(int taskId) {
        List<Score> scores = scoreMapper.listByTaskId(taskId);
        for(Score score:scores)
            scoreMapper.deleteById(score.getId());

        taskMapper.deleteByTaskId(taskId);
    }

    @Override
    public void update(Task task) {
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.update(task);
    }
}
