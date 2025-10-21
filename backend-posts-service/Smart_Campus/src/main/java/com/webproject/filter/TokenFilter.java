package com.webproject.filter;

import com.webproject.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {
    
    // 白名单路径，这些路径无需登录即可访问
    private static final List<String> WHITE_LIST = Arrays.asList(
        "/user/login",      // 用户登录
        "/users",           // 用户注册
        "/upload",          // 文件上传（头像等）
        "/static",          // 静态资源
        "/favicon.ico",     // 网站图标
        "/error"            // 错误页面
    );
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. 获取到请求路径
        String requestURI = request.getRequestURI(); // /employee/login

        //2. 判断是否是白名单请求, 如果是白名单路径, 直接放行
        if (isWhiteListPath(requestURI)) {
            log.info("白名单路径: {}, 放行", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //3. 获取请求头中的token
        String token = request.getHeader("token");

        //4. 判断token是否存在, 如果不存在, 说明用户没有登录, 返回错误信息(响应401状态码)
        if (token == null || token.isEmpty()){
            log.info("令牌为空, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //5. 如果token存在, 校验令牌, 如果校验失败 -> 返回错误信息(响应401状态码)
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            log.info("令牌非法, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //6. 校验通过, 放行
        log.info("令牌合法, 放行");
        filterChain.doFilter(request, response);
    }
    
    /**
     * 判断请求路径是否在白名单中
     * @param requestURI 请求路径
     * @return true表示在白名单中，false表示不在白名单中
     */
    private boolean isWhiteListPath(String requestURI) {
        // 检查是否包含白名单路径
        for (String whitePath : WHITE_LIST) {
            if (requestURI.contains(whitePath)) {
                return true;
            }
        }
        
        // 特殊处理：允许OPTIONS预检请求（CORS）
        return false;
    }
}
