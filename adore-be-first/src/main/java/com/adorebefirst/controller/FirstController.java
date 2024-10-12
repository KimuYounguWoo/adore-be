package com.adorebefirst.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first")
@Slf4j
public class FirstController {
    Environment env;

    public FirstController(Environment env){
        this.env = env;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome First Service";
    }

}
