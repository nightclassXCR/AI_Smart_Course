package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Resource {

    private Integer id;
    private Integer userId;
    private String name;
    private String fileUrl;
    private FileType fileType;
    private Integer ownerId;
    private OwnerType ownerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum FileType {
        pdf, ppt, doc, video, image
    }

    public enum OwnerType {
        course, task, question
    }

    public Resource() {}

    public Resource(Integer id, String name, String fileUrl, FileType fileType,
                    Integer ownerId, OwnerType ownerType, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
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
