package com.smile.pocannotation.controller;

import com.smile.pocannotation.annotation.Logging;
import com.smile.pocannotation.annotation.ValidateHeaders;
import com.smile.pocannotation.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@Slf4j
public class HelloWorldController {

    @GetMapping("/helloworld")
    @ValidateHeaders(values = {"smile"})
    @Logging
    public ResponseEntity<String> getHelloWorld() {
        return ResponseEntity.ok().headers(getResponseHeaders()).body("getting hello world success");
    }

    @PostMapping("/helloworld")
    @Logging
    public ResponseEntity<String> postHelloWorld(
            @RequestBody Object body
    ) {
        return ResponseEntity.ok().headers(getResponseHeaders()).body("posting hello world success");
    }

    private HttpHeaders getResponseHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Timestamp", String.valueOf(Instant.now().toEpochMilli()));
        return responseHeaders;
    }
}
