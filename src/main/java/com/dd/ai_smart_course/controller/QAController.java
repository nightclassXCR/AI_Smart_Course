package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.QA;
import com.dd.ai_smart_course.service.base.QAService;
import com.dd.ai_smart_course.service.impl.QAImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.service.base.DifyService;
import com.dd.ai_smart_course.R.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/qa")
public class QAController {

    @Autowired
    private QAImpl qaService;

    /**
     * 查询所有问答记录
     */
    @GetMapping("/getAll")
    public Result<List<QA>> getAllQA() {
        log.info("Received request: get all QA records");
        List<QA> data = qaService.getAllQA();
        return Result.success(data);
    }

    /**
     * 根据 ID 查询问答记录
     */
    @GetMapping("/get/{id}")
    public Result<QA> getQAById(@PathVariable long id) {
        log.info("Received request: get QA by ID: {}", id);
        QA data = qaService.getQAById(id);
        return Result.success(data);
    }

    /**
     * 添加问答记录
     */
    @PostMapping("/add")
    public Result<Integer> addQA(@RequestBody QA qa) {
        log.info("Received request: add a new QA record");
        try {
            Integer rowsAffected = qaService.addLog(qa); // 注意：在你的 QAImpl 中添加方法名为 addLog
            return Result.success(rowsAffected);
        } catch (Exception e) {
            return Result.error(500, "Failed to add QA record: " + e.getMessage());
        }
    }

    /**
     * 更新问答记录
     */
    @PostMapping("/update")
    public Result<Integer> updateQA(@RequestBody QA qa) {
        log.info("Received request: update QA record");
        try {
            Integer rowsAffected = qaService.updateQA(qa);
            return Result.success(rowsAffected);
        } catch (Exception e) {
            return Result.error(500, "Failed to update QA record: " + e.getMessage());
        }
    }

    /**
     * 删除问答记录
     */
    @GetMapping("/delete/{id}")
    public Result<Integer> deleteQA(@PathVariable int id) {
        log.info("Received request: delete QA record with ID: {}", id);
        Integer rowsAffected = qaService.deleteQA(id);
        return Result.success(rowsAffected);
    }
}