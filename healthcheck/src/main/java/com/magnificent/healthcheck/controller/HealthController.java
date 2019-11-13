package com.magnificent.healthcheck.controller;

import com.magnificent.healthcheck.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthController {
    @Autowired
    private HealthService healthService;

    @GetMapping
    private String getStatus(){
        return healthService.getStatus();
    }
}
