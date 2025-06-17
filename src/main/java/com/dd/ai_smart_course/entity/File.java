package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class File {
    private int id;
    private String name;
    private String fileUrl;
    private String fileType;
    private String ownerType; // course, chapter, task
    private int ownerId;
    private Timestamp uploadedAt;

    public File(){

    }

    public File(int id, String name, String fileUrl, String fileType, String ownerType, int ownerId, Timestamp uploadedAt) {
        this.id = id;
        this.name = name;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.uploadedAt = uploadedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
