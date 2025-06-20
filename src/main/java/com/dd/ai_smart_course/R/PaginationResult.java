package com.dd.ai_smart_course.R;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResult<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;

    public PaginationResult(List<T> content, long totalElements, int currentPage, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
    }
}