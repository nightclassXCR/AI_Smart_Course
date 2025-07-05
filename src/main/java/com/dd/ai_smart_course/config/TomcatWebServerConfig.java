//package com.dd.ai_smart_course.config;
//
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.context.annotation.Configuration;
//import java.time.Duration; // 确保导入这个
//
//@Configuration
//public class TomcatWebServerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
//
//    @Override
//    public void customize(TomcatServletWebServerFactory factory) {
//        // 通过 addContextCustomizers 添加一个定制器，用于配置 Tomcat Context
//        factory.addContextCustomizers(context -> {
//            // 在 Tomcat Context 对象上设置异步超时
//            // 这个超时是指从请求开始到所有异步操作完成的最长时间。
//            // 值是毫秒。
//            context.setAsyncTimeout(Duration.ofMinutes(4).toMillis()); // 设置为 4 分钟 (240000 毫秒)
//            System.out.println("Tomcat Async Timeout set to: " + context.getAsyncTimeout() + "ms"); // 打印确认
//        });
//
//        // 保持 connectionTimeout 的配置方式，如果之前在 application.properties 中配置并生效，则这里可以不设置
//        // 如果 application.properties 中的 connection-timeout 属性也无法解析或不生效，
//        // 则需要通过 addConnectorCustomizers 来设置，但目前我们先解决 asyncTimeout 的问题。
//        // factory.setConnectionTimeout(Duration.ofMinutes(4).toMillis()); // 示例：设置连接超时
//    }
//}