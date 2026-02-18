package com.jso.APIGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

// Import the LoadBalancer filter specifically for MVC
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes(AuthFilter authFilter) {
        return route("employee-service")
                // Professional Tip: Match all related paths in one route
                .route(path("/api/v1/employees/**", "/api/v1/departments/**"), http())
                // MODERN APPROACH: Use the Service ID registered in Eureka
                .filter(lb("employee-service"))
                .filter(authFilter.validate())
                .build()
                .and(route("iam-service")
                        .route(path("/api/v1/auth/**", "/auth/organizations/**"), http())
                        .filter(lb("iam-service"))
                        .build());
    }
}