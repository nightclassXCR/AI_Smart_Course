package com.dd.ai_smart_course.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireResponse{

    private String title;
    private String instructions;
    private List<Question> questions;

    @Data
    public static class Question {
        private String id;
        @JsonProperty("question_text")
        private String question_text;
        private String type;
        private List<String> options; // 仅单选或多选题有
        @JsonProperty("correct_answer")
        private String correct_answer;
        private String explanation;

    }
}
