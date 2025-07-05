package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.dto.ScoreDTO;
import com.dd.ai_smart_course.entity.Score;
import com.dd.ai_smart_course.service.base.ScoreService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@Slf4j
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;


    @GetMapping("/score/{id}")
    public Result<Score> getById(@PathVariable int id) {
        log.info("getById: {}", id);
        Score score = scoreService.getById(id);
        return Result.success(score);
    }

    @PostMapping("/score")
    public Result insert(@RequestBody List<Score> scores) {
        log.info("insert: {}", scores);
        scoreService.insertBatch(scores);

        return Result.success();
    }

    @DeleteMapping("/score")
    public Result delete(@RequestBody List<Integer> ids) {
        log.info("delete: {}", ids);
        scoreService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/score")
    public Result update(@RequestBody Score score) {
        log.info("update: {}", score);
        scoreService.update(score);
        return Result.success();
    }

    // 查询任务成绩
    @GetMapping("/tasks/{taskId}/scores")
    public Result<Score> getTaskScores(@PathVariable int taskId) {
        log.info("getTaskScores: {}", taskId);
        Score scores = scoreService.getTaskScore(taskId);
        return Result.success(scores);
    }

    // 查询用户成绩
    @GetMapping("/users/{userId}/scores")
    public Result<List<Score>> getUserScores(@PathVariable int userId) {
        log.info("getUserScores: {}", userId);
        List<Score> scores = scoreService.getUserScores(userId);
        return Result.success(scores);
    }

    //获取某门课程的所有学生的平均分（假设每份task权重相等）
    @GetMapping("/courses/{courseId}/finalScore")
    public Result<List<ScoreDTO>> getFinalScoreByCourseId(@PathVariable int courseId) {
        log.info("get a request: get final score by courseId: {}", courseId);
        List<ScoreDTO> scores = scoreService.getFinalScoreByCourseId(courseId);
        return Result.success(scores);
    }

    //获取某门课程的成绩分布情况（无改动）
    @GetMapping("/courses/{courseId}/pureScoreDistribution")
    public Result<List<Integer>> getPureScoreDistribution(@PathVariable int courseId) {
        log.info("get a request: get pure score distribution by courseId: {}", courseId);
        List<ScoreDTO> scores = scoreService.getFinalScoreByCourseId(courseId);
        List<Integer> distribution = scoreService.getPureScoreDistribution(scores);
        return Result.success(distribution);
    }

    //获取某门课程的成绩分布情况（比例）
    @GetMapping("/courses/{courseId}/scoreDistribution")
    public Result<List<Integer>> getScoreDistribution(@PathVariable int courseId) {
        log.info("get a request: get score distribution by courseId: {}", courseId);
        List<ScoreDTO> scores = scoreService.getFinalScoreByCourseId(courseId);
        log.info("get score list size: {}", scores.size());
        List<Integer> scoreDistribution = scoreService.getScoreDistribution(scores);
        return Result.success(scoreDistribution);
    }

}
