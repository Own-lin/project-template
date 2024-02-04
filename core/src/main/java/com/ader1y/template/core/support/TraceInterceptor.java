package com.ader1y.template.core.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.UUID;

/**
 * 链路追踪ID生成工具<tr/>
 * 实现{@link HandlerInterceptor}拓展点的拦路追踪拦截器，会在每个请求上添加traceId以便追踪日志
 * @author zhanyan
 */
@Component
public class TraceInterceptor implements HandlerInterceptor, WebMvcConfigurer {


    public static final String TRACE_ID = "TRACE_ID";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tId;
        if (StringUtils.isNotEmpty(request.getHeader(TRACE_ID))) {
            tId = request.getHeader(TRACE_ID);
        }else {
            tId = UUID.randomUUID().toString().replace("-", "");
        }

        MDC.put(TRACE_ID, tId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(TRACE_ID);
    }
}
