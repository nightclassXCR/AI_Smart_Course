package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.controller.ResourceController;
import com.dd.ai_smart_course.entity.Resource;
import com.dd.ai_smart_course.service.ResourceService;
import com.dd.ai_smart_course.utils.AliOssUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResourceControllerTest {

    private MockMvc mockMvc;
    private AliOssUtil aliOssUtil;
    private ResourceService resourceService;
    private ResourceController resourceController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        aliOssUtil = mock(AliOssUtil.class);
        resourceService = mock(ResourceService.class);
        resourceController = new ResourceController();
        objectMapper = new ObjectMapper();

        var field1 = ResourceController.class.getDeclaredField("aliOssUtil");
        field1.setAccessible(true);
        field1.set(resourceController, aliOssUtil);

        var field2 = ResourceController.class.getDeclaredField("resourceService");
        field2.setAccessible(true);
        field2.set(resourceController, resourceService);

        mockMvc = MockMvcBuilders.standaloneSetup(resourceController).build();
    }

    @Test
    public void testUploadSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());

        Resource resource = new Resource();
        String filePath = "https://oss.fake.com/" + UUID.randomUUID();

        when(aliOssUtil.upload(any(), any())).thenReturn(filePath);

        mockMvc.perform(multipart("/api/resources")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(resource))
                        .characterEncoding("UTF-8")
                        .param("file", "file")) // 支持 Multipart 参数
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(filePath));
    }

    @Test
    public void testListResources() throws Exception {
        Resource resource = new Resource();
        when(resourceService.filter(any())).thenReturn(List.of(resource));

        mockMvc.perform(get("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testDeleteResourceSuccess() throws Exception {
        Resource resource = new Resource();
        resource.setFileUrl("oss://path/test.pdf");

        when(resourceService.findById(1L)).thenReturn(resource);
        doNothing().when(aliOssUtil).delete(resource.getFileUrl());
        doNothing().when(resourceService).delete(1L);

        mockMvc.perform(delete("/api/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    @Test
    public void testDeleteResourceNotFound() throws Exception {
        when(resourceService.findById(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/resources/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("资源不存在"));
    }

    @Test
    public void testDownloadResource() throws Exception {
        Resource resource = new Resource();
        resource.setName("文件.pdf");
        resource.setFileUrl("oss://fake/file.pdf");

        when(resourceService.findById(1L)).thenReturn(resource);
        when(aliOssUtil.download(resource.getFileUrl()))
                .thenReturn(new ByteArrayInputStream("测试内容".getBytes()));

        mockMvc.perform(get("/api/resources/download/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=%E6%96%87%E4%BB%B6.pdf"))
                .andExpect(content().contentType("application/octet-stream"));
    }

    @Test
    public void testDownloadNotFound() throws Exception {
        when(resourceService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/resources/download/999"))
                .andExpect(status().isNotFound());
    }
}
