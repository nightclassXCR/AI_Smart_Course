package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Task_question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskQuestionMapper {

    @Select("SELECT * FROM task_question WHERE task_id = #{task_id}")
    List<Task_question> listByTaskId(int task_id);
    //作业新增题目
    @Insert("INSERT INTO task_question (task_id, question_id, max_score, sequence) VALUES (#{task_id}, #{question_id}, #{max_score}, #{sequence})")
    int insert(Task_question task_question);

    //作业删除题目
    @Delete("DELETE FROM task_question WHERE task_id = #{task_id} AND question_id = #{question_id}")
    int delete(Task_question task_question);

    //作业更新题目
    @Update("UPDATE task_question SET max_score = #{max_score}, sequence = #{sequence} WHERE task_id = #{task_id} AND question_id = #{question_id}")
    int update(Task_question task_question);

    //批量添加
    int insertBatch(@Param("task_questions") List<Task_question> task_questions);

    //批量删除
    int deleteBatch(@Param("task_ids")Integer task_ids);
}
