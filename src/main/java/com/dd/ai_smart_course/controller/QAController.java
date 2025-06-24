package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.service.QAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.service.DifyService;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.service.DifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.time.LocalDateTime;
import java.util.Map;



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