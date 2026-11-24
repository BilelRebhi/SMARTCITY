package com.campusverse.municipal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MunicipalApplication {
    public static void main(String[] args) {
        SpringApplication.run(MunicipalApplication.class, args);
    }
}

