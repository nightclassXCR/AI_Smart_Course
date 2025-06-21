package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Score;
import com.dd.ai_smart_course.mapper.ScoreMapper;
import com.dd.ai_smart_course.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
}
