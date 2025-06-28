package com.dd.ai_smart_course.interceptor;

import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.utils.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationInterceptor.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        // 1. 从请求头获取token
        String token = request.getHeader("Authorization");

        // 2. 检查token是否存在
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("缺少认证token");
            return false;
        }

        // 3. 验证token格式 (Bearer token)
        if (!token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token格式错误");
            return false;
        }

        // 4. 提取实际token (去掉Bearer前缀)
        String actualToken = token.substring(7);

        try {
            // 5. 验证token有效性
            if (jwtTokenUtil.isTokenExpired(actualToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("token已过期");
                return false;
            }

            // 6. 从token中获取用户ID
            Integer userId = jwtTokenUtil.getUserIDFromToken(actualToken);
            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("无效的token");
                return false;
            }
            BaseContext.setCurrentId(userId);

            // 7. 将用户ID存入request属性，供后续使用
            request.setAttribute("userId", userId);

            return true;

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token验证失败: " + e.getMessage());
            return false;
        }
    }
}
