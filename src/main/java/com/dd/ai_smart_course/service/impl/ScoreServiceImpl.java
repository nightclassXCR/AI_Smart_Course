package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.ScoreDTO;
import com.dd.ai_smart_course.entity.Score;
import com.dd.ai_smart_course.mapper.ScoreMapper;
import com.dd.ai_smart_course.service.base.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public Score getById(int Id) {
        Score score = scoreMapper.getById(Id);
        return score;
    }

    @Override
    public void insertBatch(List<Score> scores) {
        scoreMapper.insertBatch(scores);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        scoreMapper.deleteByIds(ids);

    }

    @Override
    public void update(Score score) {
        score.setGradedAt(LocalDateTime.now());
        scoreMapper.update(score);
    }

    @Override
    public Score getTaskScore(int taskId) {
        List<Score> scores = scoreMapper.listByTaskId(taskId);
        return scores.size() > 0 ? scores.get(0) : null;
    }

    @Override
    public List<Score> getUserScores(int userId) {
        List<Score> scores = scoreMapper.getScoreByUserId(userId);
        return scores;
    }

    //获取某门课程的所有学生的平均分（假设每份task权重相等）
    @Override
    public List<ScoreDTO> getFinalScoreByCourseId(int courseId) {
        List<ScoreDTO> scores = scoreMapper.getFinalScoreByCourseId(courseId);
        return scores;
    }

    //获取学生的分数分布
    @Override
    public List<Integer> getPureScoreDistribution(List<ScoreDTO>  scores) {
        List<Integer> scoreDistribution = new ArrayList<>();
        int excellentNum = (int) scores.stream()
                .filter(score -> score.getFinalScore().compareTo(BigDecimal.valueOf(90)) >= 0)
                .count();

        int goodNum = (int) scores.stream()
                .filter(score -> score.getFinalScore().compareTo(BigDecimal.valueOf(80)) >= 0
                        && score.getFinalScore().compareTo(BigDecimal.valueOf(90)) < 0)
                .count();

        int averageNum = (int) scores.stream()
                .filter(score -> score.getFinalScore().compareTo(BigDecimal.valueOf(70)) >= 0
                        && score.getFinalScore().compareTo(BigDecimal.valueOf(80)) < 0)
                .count();

        int poorNum = (int) scores.stream()
                .filter(score -> score.getFinalScore().compareTo(BigDecimal.valueOf(60)) >= 0
                        && score.getFinalScore().compareTo(BigDecimal.valueOf(70)) < 0)
                .count();

        int failNum = (int) scores.stream()
                .filter(score -> score.getFinalScore().compareTo(BigDecimal.valueOf(60)) < 0)
                .count();

        scoreDistribution.add(excellentNum);
        scoreDistribution.add(goodNum);
        scoreDistribution.add(averageNum);
        scoreDistribution.add(poorNum);
        scoreDistribution.add(failNum);

        return scoreDistribution;
    }

    //获取学生的分数分布（百分比）
    @Override
    public List<Integer> getScoreDistribution(List<ScoreDTO>  scores) {
        List<Integer> scoreDistribution = getPureScoreDistribution(scores);
        int totalNum = scoreDistribution.stream().mapToInt(Integer::intValue).sum();
        log.info("totalNum: {}", totalNum);

        //判断人数是否为0
        if(totalNum == 0){
            scoreDistribution.replaceAll(num -> 0);
            return scoreDistribution;
        }

        //按照百分比
        scoreDistribution.replaceAll(num -> num * 100 / totalNum);
        return scoreDistribution;
    }
}
