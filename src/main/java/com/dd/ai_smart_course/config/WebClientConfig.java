package com.dd.ai_smart_course.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        // 配置 Reactor Netty 的 HttpClient
        HttpClient httpClient = HttpClient.create()
                // 连接超时：建立连接的最大时间（例如 10 秒）
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                // 响应超时：从发送请求到接收完整响应的总时间
                // 设置为比 Dify 实际完成时间稍长，例如 4 分 30 秒
                .responseTimeout(Duration.ofMinutes(8).plusSeconds(30))
                // 当连接建立后，进一步配置读写超时处理器
                .doOnConnected(conn -> conn
                        // 读取超时：连接空闲时间（没有数据读入）超过此时间则超时
                        // 这对于流式响应很重要，防止Dify在发送数据块之间挂起
                        .addHandlerLast(new ReadTimeoutHandler(Duration.ofMinutes(8).toSeconds() + 30, TimeUnit.SECONDS))
                        // 写入超时：发送请求体（如果很大）的最大时间
                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)));

        // 使用配置好的 HttpClient 来创建 WebClient.Builder
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
