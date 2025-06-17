package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.Score;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScoreMapper {

    @Insert("INSERT INTO score (id,userId,taskId,grade,comment,submitTime)" +
            "VALUES (#{id},#{userId},#{taskId},#{grade},#{comment},#{submitTime})")
    int addScore(Score score);

    @Delete("DELETE FROM score WHERE id = #{id}")
    int deleteScore(int id);

    @Select("SELECT * FROM score")
    List<Score> gerAllScore();

    @Select("SELECT * FROM score WHERE id =#{id}")
    Score getScoreById(int id);

    @Update("UPDATE score SET id =#{id},userId =#{userId},taskId =#{taskId},grade =#{grade},comment =#{comment},submitTime =#{submitTime}")
    int updateScore(Score score);

}
