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
@ActiveProfiles("test") // 确保使用测试配置文件
public class AnalysisControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    // ***** 核心修改：将这里的 Token 替换为你应用程序实际生成的有效 Token *****
    // 步骤：
    // 1. 运行你的 Spring Boot 应用。
    // 2. 使用你的登录接口，用一个测试用户（例如，ID=1，用户名=testuser）登录。
    // 3. 从登录接口的响应中获取真实的 Authorization Bearer Token 字符串。
    // 4. 将获取到的完整字符串（包括 "Bearer " 前缀）粘贴到下面。
    // 例如：private String validJwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJleHAiOjE2Nzg4ODg4ODh9.VERY_LONG_REAL_SIGNATURE_GENERATED_BY_YOUR_APP";
    private String validJwtToken = "Bearer <将你应用程序真实生成的JWT Token粘贴到这里>"; // <<< 请务必替换此占位符！

    // --- 修改部分 ---



    @Test
    public void testGetAllLearningLogs() {
        // 这个接口是管理员功能，假设不需要JWT，但如果需要，同样添加 header
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        // 路径已在上次修改中更正为 /analysis/all-logs
                        .path("/analysis/all-logs")
                        .queryParam("userId", "1") // userId是必需的参数
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("获取成功")
                .jsonPath("$.data").exists();
    }

    @Test
    public void testCountAllLearningLogs() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        // 路径已在上次修改中更正为 /analysis/all-logs/count
                        .path("/analysis/all-logs/count")
                        .queryParam("userId", "1")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(result -> {
                    System.out.println("响应状态码：" + result.getStatus());
                    if (result.getResponseBody() != null) {
                        String responseBody = new String(result.getResponseBody());
                        System.out.println("响应内容：" + responseBody);
                    } else {
                        System.out.println("响应内容为空");
                    }
                })
                .jsonPath("$.message").isEqualTo("统计成功") // 字段名已在上次修改中更正为 message
                .jsonPath("$.data").isNumber();
    }



    @Test
    public void testGetUserLearningStats() {
        // 路径已在上次修改中更正为 /analysis/user-stats/123
        webTestClient.get()
                .uri("/analysis/user-stats/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("获取成功")
                .jsonPath("$.data").exists();
    }

    @Test
    public void testNotFoundEndpoint() {
        webTestClient.get()
                .uri("/analysis/non-existent-path") // 访问一个不存在的路径
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound(); // 预期 404 Not Found
    }
}