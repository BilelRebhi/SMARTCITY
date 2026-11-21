package com.smartcity.urbanincident;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UrbanIncidentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanIncidentApplication.class, args);
    }
}
