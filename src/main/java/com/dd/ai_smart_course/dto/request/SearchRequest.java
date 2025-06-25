package com.dd.ai_smart_course.dto.request;

import lombok.Data;

@Data
public class SearchRequest {
    private String subject;         //主题
    private String compareParam;    //用于比较的参数
    private boolean isDESC;         //是否逆序
    private String order;           //排序指标
    private Integer limit;              //搜索元素数量
    private Integer offset;             //偏移量
}
