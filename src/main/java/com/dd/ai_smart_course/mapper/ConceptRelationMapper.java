package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.ConceptRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConceptRelationMapper {
    @Insert("INSERT INTO concept_relation (source_concept_id, target_concept_id, relation_type) VALUES (#{sourceConceptId}, #{targetConceptId}, #{relationType})")
    int addRelation(ConceptRelation relation);

    @Delete("DELETE FROM concept_relation WHERE id = #{id}")
    int deleteRelation(int id);

    @Select("SELECT * FROM concept_relation WHERE source_concept_id = #{conceptId} OR target_concept_id = #{conceptId}")
    List<ConceptRelation> getRelationsByConcept(int conceptId);
}