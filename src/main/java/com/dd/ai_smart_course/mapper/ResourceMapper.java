package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Resource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResourceMapper {



    void insert(Resource resource);

    @Select("SELECT * FROM resources WHERE id = #{id}")
    Resource selectById(Long id);

    @Delete("DELETE FROM resources WHERE id = #{id}")
    void deleteById(Long id);


    List<Resource> selectByFilter(Resource resource);


}
