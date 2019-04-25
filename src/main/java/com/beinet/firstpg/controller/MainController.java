package com.beinet.firstpg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("")
public class MainController {
    @GetMapping("/")
    public String getTime(){
        return LocalDateTime.now().toString();
    }
}
