package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.service.base.QAService;
import org.springframework.beans.factory.annotation.Autowired;

import com.dd.ai_smart_course.service.dto.QuestionDTO;
import com.dd.ai_smart_course.service.base.DifyService;
import com.dd.ai_smart_course.R.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/qa")
public class QAController {

    @Autowired
    private QAService qaService;

    @Autowired
    private DifyService difyService;

    /**
     * 使用 Dify 智能体回答问题的新接口
     */
    @PostMapping("/ask")
    public Result<String> askWithDify(@RequestBody QuestionDTO questionDTO) {
        String answer = difyService.getAnswerFromDify(questionDTO.getContent()); // 使用正确字段
        return Result.success(answer);
    }
}