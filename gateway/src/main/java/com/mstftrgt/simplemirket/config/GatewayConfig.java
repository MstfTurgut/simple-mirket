package com.mstftrgt.simplemirket.config;

import com.mstftrgt.simplemirket.filter.ApiRedirectGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final ApiRedirectGatewayFilterFactory apiRedirectFilter;

    public GatewayConfig(ApiRedirectGatewayFilterFactory apiRedirectFilter) {
        this.apiRedirectFilter = apiRedirectFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("api-proxy", r -> r
                        .path("/proxy/{apiName}/**")
                        .filters(f -> f
                                .filter(apiRedirectFilter.apply(new Object()), 10001))
                        .uri("https://api.genderize.io"))
                .build();
    }
}