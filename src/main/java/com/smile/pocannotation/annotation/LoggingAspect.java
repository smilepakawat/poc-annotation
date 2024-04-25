package com.smile.pocannotation.annotation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {
    private final HttpServletRequest request;

    public LoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Around("@annotation(com.smile.pocannotation.annotation.Logging)")
    public Object logging(ProceedingJoinPoint jp) throws Throwable {
        log.info("header {}", getRequestHeaders(request));
        log.info("body {}", getPayload(jp));
        return jp.proceed();
    }

    private String getPayload(JoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            builder.append(joinPoint.getArgs()[i].toString());
            builder.append(", ");
        }
        return builder.toString();
    }

    private String getRequestHeaders(HttpServletRequest request) {
        JSONObject headers = new JSONObject();
        request.getHeaderNames().asIterator().forEachRemaining(entry -> headers.put(entry, request.getHeader(entry)));
        return headers.toString();
    }
}
