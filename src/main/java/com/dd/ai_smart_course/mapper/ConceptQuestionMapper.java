package com.dd.ai_smart_course.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConceptQuestionMapper {


    @Select("SELECT concept_id FROM concept_question WHERE question_id = #{questionId}")
    List<Long> findConceptIdsByTaskId(Long taskId);
}
