package com.smile.pocannotation.controller;

import com.smile.pocannotation.annotation.Logging;
import com.smile.pocannotation.annotation.ValidateHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/helloworld")
    @ValidateHeaders(values = {"smile"})
    @Logging
    public ResponseEntity<String> getHelloWorld() {
        return ResponseEntity.ok().body("getting hello world success");
    }

    @PostMapping("/helloworld")
    @ValidateHeaders(values = {"smile"})
    @Logging
    public ResponseEntity<String> postHelloWorld() {
        return ResponseEntity.ok().body("posting hello world success");
    }
}
