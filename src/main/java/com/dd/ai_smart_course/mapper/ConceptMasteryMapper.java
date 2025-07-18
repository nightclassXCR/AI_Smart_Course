package com.dd.ai_smart_course.mapper;


import com.dd.ai_smart_course.entity.ConceptMastery;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ConceptMasteryMapper {
    // 根据用户ID和概念ID查找
    @Select("SELECT * FROM concept_mastery WHERE user_id = #{userId} AND concept_id = #{conceptId}")
    Optional<ConceptMastery> findByUserIdAndConceptId(@Param("userId") int userId, @Param("conceptId") int conceptId);

    // 用于定时任务获取所有记录或特定用户/概念的记录
    @Select("SELECT * FROM concept_mastery")
    List<ConceptMastery> findAllMasteries();

    // 用于获取特定用户
    @Select("SELECT * FROM concept_mastery WHERE user_id = #{userId}")
    List<ConceptMastery> findMasteriesByUserId(@Param("userId") int userId);

    // 用于获取特定用户和概念
    ConceptMastery findConceptMasteryByUserIdAndConceptId(@Param("userId") Integer userId, @Param("conceptId") Integer conceptId);

    void insertConceptMastery(ConceptMastery conceptMastery);

    void updateConceptMastery(ConceptMastery conceptMastery);

    List<ConceptMastery> findAllConceptMasteries();


}
