package com.dd.ai_smart_course.service.dto;

import java.time.LocalDateTime;

public class UserCourseDTO {
    private Long userId;
    private Long courseId;
    private String username;
    private String courseName;
    private String courseCoverUrl;
    private String courseDescription;
    private String teacherName;
    private Long teacherId;

    // 用户与课程的关联状态
    //private LearningStatus learningStatus; // 枚举：NOT_STARTED, IN_PROGRESS, COMPLETED, DROPPED
    private Double progress; // 0.0 - 1.0
    private LocalDateTime lastLearnedAt;
    private LocalDateTime startedLearningAt;
    private LocalDateTime completedAt;

    // 课程的额外属性
    private Integer totalLessons;
    private Integer completedLessons;
    //private CourseDifficulty difficulty; // 枚举：BEGINNER, INTERMEDIATE, ADVANCED
    private Double rating;
    private Integer enrollmentCount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCoverUrl() {
        return courseCoverUrl;
    }

    public void setCourseCoverUrl(String courseCoverUrl) {
        this.courseCoverUrl = courseCoverUrl;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public LocalDateTime getLastLearnedAt() {
        return lastLearnedAt;
    }

    public void setLastLearnedAt(LocalDateTime lastLearnedAt) {
        this.lastLearnedAt = lastLearnedAt;
    }

    public LocalDateTime getStartedLearningAt() {
        return startedLearningAt;
    }

    public void setStartedLearningAt(LocalDateTime startedLearningAt) {
        this.startedLearningAt = startedLearningAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public Integer getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(Integer completedLessons) {
        this.completedLessons = completedLessons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(Integer enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }
}
