package com.cicd.ansible.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/test")
    public String retrieveLimits(){
        return "Hello Hi";
    }
}
