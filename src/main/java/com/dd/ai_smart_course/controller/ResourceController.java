package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.Resource;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.service.base.ResourceService;
import com.dd.ai_smart_course.utils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
@Slf4j

public class ResourceController {


    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private ResourceService resourceService;

    @PostMapping
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file, @RequestBody Resource resource) {
        log.info("文件上传：{}", file);
        try {
            String originalFileName = file.getOriginalFilename();
            //截取原始文件名后缀
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            resourceService.save(resource);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
        }
        return Result.error("文件上传失败");
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
    @DeleteMapping("/{id}")
    @ApiOperation("删除资源")
    public Result<Void> delete(@PathVariable Long id) {
        Resource resource = resourceService.findById(id);
        if (resource == null) {
            return Result.error("资源不存在");
        }

        aliOssUtil.delete(resource.getFileUrl()); // 假设 AliOssUtil 有 delete(String path) 方法
        resourceService.delete(id);

        return Result.success();
    }

    /**
     * 下载资源
     */
    @GetMapping("/download/{id}")
    @ApiOperation("下载资源")
    public void download(@PathVariable Long id, HttpServletResponse response) {
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


}
