package com.dd.ai_smart_course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionQuestion {
    private String id;
    private String question_text; // 题目内容，用于日志或前端展示
    private String type;
    private List<String> options; // 选项，用于日志或前端展示
    private String correct_answer; // 正确答案，后端用来判断
    private String explanation; // 知识点，后端用来更新掌握度

    private String userAnswer; // 用户提交的答案

}
