package com.adorebesecond.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second")
@Slf4j
public class SecondController {
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome Second Service";
    }
}
