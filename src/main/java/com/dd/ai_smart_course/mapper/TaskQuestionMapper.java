package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Task_question;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskQuestionMapper {

    @Insert("insert into task_question (task_id, question_id) values (#{taskId}, #{questionId})")
    void insert(Task_question tq);

    @Delete("delete from task_question where task_id=#{taskId}")
    void deleteBatch(int taskId);
}
