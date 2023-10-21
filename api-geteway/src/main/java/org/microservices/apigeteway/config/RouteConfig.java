package org.microservices.apigeteway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder ) {
        return  builder.routes()
                .route("product-service",
                        r->r.path("/api/product/v1/**")
                                .uri("lb://product-service") )
                .route("order-service",
                        r-> r.path("/api/order/v1/**")
                                .uri("lb://order-service"))
                .build();
    }
}
