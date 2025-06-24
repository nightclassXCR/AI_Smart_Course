package com.dd.ai_smart_course.entity;

import java.time.LocalDateTime;

public class Resource {

    private Long id;
    private String name;
    private String fileUrl;
    private FileType fileType;
    private Long ownerId;
    private OwnerType ownerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum FileType {
        PDF, PPT, DOC, VIDEO, IMAGE
    }

    public enum OwnerType {
        COURSE, CHAPTER, TASK, QUESTION
    }

    public Resource() {}

    public Resource(Long id, String name, String fileUrl, FileType fileType,
                    Long ownerId, OwnerType ownerType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileType=" + fileType +
                ", ownerId=" + ownerId +
                ", ownerType=" + ownerType +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
