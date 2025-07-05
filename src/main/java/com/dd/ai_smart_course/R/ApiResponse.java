package com.dd.ai_smart_course.R;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success; // 请求是否成功
    private T data;          // 成功时携带的数据
    private String message;  // 消息或错误信息
    private Integer code;
}
