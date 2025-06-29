package com.dd.ai_smart_course.dto;

import com.dd.ai_smart_course.entity.Resource;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceDTO {

    private Integer id;
    private Integer userId;
    private String name;
    private String fileUrl;
    private Resource.FileType fileType;
    private Integer ownerId;
    private String ownerName;
    private Resource.OwnerType ownerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
