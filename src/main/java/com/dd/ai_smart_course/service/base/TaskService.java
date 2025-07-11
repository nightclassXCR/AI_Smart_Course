package com.dd.ai_smart_course.service.base;


import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.dto.SubmitAnswerDTO;
import com.dd.ai_smart_course.dto.TaskAnswerResultDTO;
import com.dd.ai_smart_course.dto.TaskDTO;
import com.dd.ai_smart_course.entity.Task;

import java.util.List;

public interface TaskService {


    void insert(TaskDTO taskDTO);

    List<Task> listByCourseId(int courseId);

    void delete(int taskId);

    void update(Task task);

//    List<Integer> findQuestionIdsByTaskId(int taskId);

    List<Task> listByUserId(int userId);

    Integer getTaskCountByTeacherId(int teacherId);

    // 获取任务下的所有问题
    List<QuestionDTO> getQuestionsByTaskId(int taskId);
    // **【重点】添加这一行代码到接口中**
    List<TaskAnswerResultDTO> submitAnswerAndJudge(List<SubmitAnswerDTO> submitAnswerDTOList);
}
