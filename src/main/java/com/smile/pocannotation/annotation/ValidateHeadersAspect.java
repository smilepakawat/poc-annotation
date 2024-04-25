package com.smile.pocannotation.annotation;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ValidateHeadersAspect {
    Logger logger = LoggerFactory.getLogger(ValidateHeadersAspect.class);
    private final @NonNull HttpServletRequest request;

    @Around("@annotation(com.smile.pocannotation.annotation.ValidateHeaders)")
    public Object validateAspect(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        ValidateHeaders validateHeaders = method.getAnnotation(ValidateHeaders.class);
        if (Boolean.FALSE.equals(isValidateHeaders(validateHeaders))) {
            return ResponseEntity.badRequest().body("missing mandatory fields");
        }
        return jp.proceed();
    }

    private Boolean isValidateHeaders(ValidateHeaders validateHeaders) {
        String[] values = validateHeaders.values();
        for (String s : values) {
            if (getAllHeaders().stream().noneMatch(e -> e.equalsIgnoreCase(s))) {
                return false;
            }
        }
        return true;
    }

    private List<String> getAllHeaders() {
        List<String> header = new ArrayList<>();
        request.getHeaderNames().asIterator().forEachRemaining(header::add);
        return header;
    }
}
