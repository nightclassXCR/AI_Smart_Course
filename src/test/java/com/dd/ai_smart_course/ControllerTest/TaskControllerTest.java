package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.controller.TaskController;
import com.dd.ai_smart_course.entity.Task;
import com.dd.ai_smart_course.service.base.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    private MockMvc mockMvc;
    private TaskService taskService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        taskService = mock(TaskService.class);

        TaskController controller = new TaskController();
        var field = TaskController.class.getDeclaredField("taskService");
        field.setAccessible(true);
        field.set(controller, taskService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testListByCourseId() throws Exception {
        List<Task> tasks = List.of(new Task(), new Task());
        when(taskService.listByCourseId(1)).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testDeleteTask() throws Exception {
        doNothing().when(taskService).delete(1);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task task = new Task();
        task.setTitle("Updated Task");
        doNothing().when(taskService).update(any());

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("操作成功"));
    }
}
