package org.microservices.apigeteway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/view")
public class ControllerManagement {

    @PostMapping("/")
    public String hello(){
        return "hello";
    }
}
