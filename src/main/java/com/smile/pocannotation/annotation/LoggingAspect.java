package com.smile.pocannotation.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoggingAspect {
    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final @NonNull HttpServletRequest request;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.smile.pocannotation.annotation.Logging)")
    public Object logging(ProceedingJoinPoint jp) throws Throwable {
        logger.info("headers : {} body : {}", getHeaders(request), getBody(request));
        return jp.proceed();
    }

    private String getBody(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        String contentType = request.getContentType();
        return contentType == null ? new HashMap<>().toString() : objectMapper.readValue(body, Object.class).toString();
    }

    private String getHeaders(HttpServletRequest request) {
        Map<String, String> header = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(entry -> header.put(entry, request.getHeader(entry)));
        return header.toString();
    }
}
