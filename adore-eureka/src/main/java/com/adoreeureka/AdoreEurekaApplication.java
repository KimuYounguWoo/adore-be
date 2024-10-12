package com.adoreeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AdoreEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdoreEurekaApplication.class, args);
    }

}
