package com.dd.ai_smart_course.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DifyCompletionResponse {
    @JsonProperty("workflow_run_id")
    private String workflowRunId;
    @JsonProperty("task_id")
    private String taskId;
    private Data data;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private String id;
        @JsonProperty("workflow_id")
        private String workflowId;
        private String status; // 工作流执行状态: running / succeeded / failed / stopped
        private DifyWorkflowOutput outputs; // <<< 关键：这里直接映射为您需要的 KnowledgeGraphResponse >>>
        private String error; // 错误信息
        @JsonProperty("elapsed_time")
        private Double elapsedTime;
        @JsonProperty("total_tokens")
        private Integer totalTokens;
        @JsonProperty("total_steps")
        private Integer totalSteps;
        @JsonProperty("created_at")
        private Long createdAt;
        @JsonProperty("finished_at")
        private Long finishedAt;
    }

}
