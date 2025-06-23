package com.dd.ai_smart_course.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConceptQuestionMapper {

    // 获取概念ID
    @Select("SELECT concept_id FROM concept_question WHERE question_id = #{questionId}")
    List<Long> findConceptIdsByTaskId(Long taskId);

    // 添加概念和任务关联
    @Insert("INSERT INTO concept_question (concept_id, question_id) VALUES (#{conceptId}, #{questionId})")
    void linkConceptToQuestion(Long conceptId, Long questionId);
}
