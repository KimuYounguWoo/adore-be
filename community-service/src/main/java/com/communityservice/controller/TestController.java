package com.communityservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community")
public class TestController {

    @GetMapping("/welcome")
    public String welcome() {
        return "커뮤니티 서비스입니다.";
    }

}