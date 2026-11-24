package com.awd.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                // MS6 - Smart Places Service
                .route("smart-places-service", r -> r
                        .path("/api/smartPlaces", "/api/smartPlaces/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://smart-places-service"))

                // MS2 - Urban Incident Service
                .route("urban-incident", r -> r
                        .path("/api/incidents", "/api/incidents/**",
                              "/api/interventions", "/api/interventions/**",
                              "/api/work-orders", "/api/work-orders/**",
                              "/api/notifications", "/api/notifications/**",
                              "/api/resolution-confirmations", "/api/resolution-confirmations/**")
                        .uri("lb://urban-incident"))

                // MS5 - Digital Municipal Services
                .route("municipal-service", r -> r
                        .path("/api/municipal", "/api/municipal/**")
                        .uri("lb://municipal-service"))

                .build();
    }
}