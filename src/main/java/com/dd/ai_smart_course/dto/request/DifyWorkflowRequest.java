package com.dd.ai_smart_course.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DifyWorkflowRequest {
    private Map<String, Object> inputs; // 对应 Dify 工作流的输入变量
    @JsonProperty("response_mode")
    private String responseMode; // 响应模式 (streaming 或 blocking)
    private String user; // 用户标识符
}
