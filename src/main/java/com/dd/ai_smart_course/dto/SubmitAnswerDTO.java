package com.dd.ai_smart_course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

/**
 * 用户提交答题的数据传递对象
 */
@Data
public class SubmitAnswerDTO {

    @NotNull(message = "任务ID不能为空")
    @Min(value = 1, message = "任务ID必须大于0")
    private int taskId;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private int userId;

    @NotNull(message = "问题ID不能为空")
    @Min(value = 1, message = "问题ID必须大于0")
    private int questionId;
    private String userAnswer; // 用户选择的选项（A、B、C、D等）
}