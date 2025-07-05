package com.dd.ai_smart_course.dto;


import lombok.Data;

import java.util.List;

@Data
public class KnowledgeGraphResponse {
    private String id; // 课程的唯一ID，如 "comp_arch_101"
    private String type; // 类型标识，如 "course"
    private String title; // 课程标题
    private String description; // 课程描述
    private List<ChapterNode> chapters;


    @Data
    public static class ChapterNode {
        private String id; // 章节的ID，如 "cpu_chapter_0"
        private String type; // 类型标识，如 "chapter"
        private String title; // 章节标题
        private String content; // 章节内容或描述
        private List<KnowledgePointNode> children; // 嵌套的知识点列表
    }

    /**
     * 内部类，匹配 Dify 输出中的知识点对象
     */
    @Data
    public static class KnowledgePointNode {
        private String id; // 知识点的ID，如 "cpu_composition"
        private String type; // 类型标识，如 "knowledge_point"
        private String name; // 知识点名称
        private String description; // 知识点描述
        private String importance; // 重要性 (low, medium, high)
    }
}
