package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.dto.ResourceDTO;
import com.dd.ai_smart_course.entity.Resource;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.mapper.ResourceMapper;
import com.dd.ai_smart_course.service.base.ResourceService;
import com.dd.ai_smart_course.utils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resource")
@Slf4j

public class ResourceController {


    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("文件上传")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam String ownerName,
            @RequestParam String ownerType,
            @RequestParam String name,
            @RequestParam String type) {

        try {
            Resource resource = new Resource();
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            resource.setFileUrl(filePath);
            resource.setName(name);  // 使用前端传来的name
            resource.setFileType(Resource.FileType.valueOf(type));  // 使用前端传来的type
            log.info("上传成功，ownerId：{}", resource.getUserId());
            resource.setOwnerId(resourceMapper.findIdByNameAndType(ownerName, ownerType));
            resource.setOwnerType(Resource.OwnerType.valueOf(ownerType));
            resourceService.save(resource);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
            return Result.error("文件上传失败");
        }
    }

    /**
     * 查询资源
     */
    @GetMapping
    @ApiOperation("查询资源")
    public Result<List<Resource>> list(@RequestBody Resource resource) {
        List<Resource> resources = resourceService.filter(resource);
        return Result.success(resources);
    }

    /**
     * 删除资源
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除资源")
    public Result<Void> delete(@PathVariable int id) {
        Resource resource = resourceService.findById(id);
        if (resource == null) {
            return Result.error("资源不存在");
        }

        aliOssUtil.delete(resource.getFileUrl()); // 假设 AliOssUtil 有 delete(String path) 方法
        resourceService.delete(id);

        return Result.success();
    }

    @GetMapping("/chapter/{chapterId}")
    public Result<List<Resource>> getResourcesByChapterId(@PathVariable int chapterId) {
       List<Resource> resources = resourceService.listByChapterId(chapterId);
       log.info("查询成功，chapterId：{}", chapterId);
       log.info("查询成功，resources：{}", resources);
        return Result.success(resources);
    }


    /**
     * 下载资源
     */
    @GetMapping("/download/{id}")
    @ApiOperation("下载资源")
    public void download(@PathVariable int id, HttpServletResponse response) {
        Resource resource = resourceService.findById(id);
        if (resource == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try (InputStream inputStream = aliOssUtil.download(resource.getFileUrl())) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(resource.getName(), "UTF-8"));

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error("下载失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public Result <List<Resource>> list(){
        List<Resource> resources=resourceService.list();
        return Result.success(resources);
    }

    /**
     * 用户查看或播放资源的行为上报接口
     */
    @PostMapping("/{resourceId}/users/view")
    public Result viewOrPlayResource(
            HttpServletRequest request,
            @PathVariable("resourceId") int resourceId,
            @RequestParam(value = "durationSeconds", required = false) Integer durationSeconds,
            @RequestParam(value = "currentProgress", required = false) String currentProgress,
            @RequestParam(value = "resourceType", required = false) String resourceType
    ) {
        String Header = request.getHeader("Authorization");
        String token = Header != null ? Header.replace("Bearer ", "").trim() : null;
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        resourceService.viewOrPlayResource(resourceId, userId, durationSeconds, currentProgress, resourceType);
        return Result.success("资源行为上报成功");
    }


}
