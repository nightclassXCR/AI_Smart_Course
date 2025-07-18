package com.dd.ai_smart_course.dto.request;

import com.dd.ai_smart_course.dto.SubmissionQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuizSubmissionRequest {
    private int userId;
    private List<SubmissionQuestion> submittedQuestions;

}
