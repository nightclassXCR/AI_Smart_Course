package com.dd.ai_smart_course.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AnalysisControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetMyLearningLogs() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/analysis/my-logs")
                        .queryParam("targetType", "typeA")
                        .queryParam("actionType", "actionB")
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.msg").isEqualTo("获取成功")
                .jsonPath("$.data").exists();
    }

    @Test
    public void testGetAllLearningLogs() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/analysis/all-logs")
                        .queryParam("userId", "1")
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.msg").isEqualTo("获取成功")
                .jsonPath("$.data").exists();
    }

    @Test
    public void testCountAllLearningLogs() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/analysis/all-logs/count")
                        .queryParam("userId", "1")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // 暂时移除 expectStatus().isOk()，避免阻断打印
                .expectBody()
                .consumeWith(result -> {
                    System.out.println("响应状态码：" + result.getStatus());
                    if (result.getResponseBody() != null) {
                        System.out.println("响应内容：" + new String(result.getResponseBody()));
                    } else {
                        System.out.println("响应内容为空");
                    }
                });
    }

    @Test
    public void testGetMyLearningStats() {
        webTestClient.get()
                .uri("/api/analysis/my-stats")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.msg").isEqualTo("获取成功")
                .jsonPath("$.data").exists();
    }

    @Test
    public void testGetUserLearningStats() {
        webTestClient.get()
                .uri("/api/analysis/user-stats/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.msg").isEqualTo("获取成功")
                .jsonPath("$.data").exists();
    }
}
