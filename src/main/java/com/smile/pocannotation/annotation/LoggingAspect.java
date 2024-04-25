package com.smile.pocannotation.annotation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

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
        log.info("before {}", constructLogMsg(jp));
        Object proceed = jp.proceed();
        log.info("after {}", proceed.toString());
        return proceed;
    }

    private String constructLogMsg(JoinPoint jp) {
        var args = Arrays.stream(jp.getArgs()).map(String::valueOf).collect(Collectors.joining(",", "", ""));
        return request.getMethod() + "@" + request.getRequestURI() + ":" + getRequestHeaders(request) + args;
    }

    private String getRequestHeaders(HttpServletRequest request) {
        JSONObject headers = new JSONObject();
        request.getHeaderNames().asIterator().forEachRemaining(entry -> headers.put(entry, request.getHeader(entry)));
        return headers.toString();
    }
}
