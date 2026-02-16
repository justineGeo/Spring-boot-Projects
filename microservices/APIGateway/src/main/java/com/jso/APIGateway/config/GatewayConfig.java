package com.jso.APIGateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

// New required imports for the "Before" URI approach
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;

import java.net.URI;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes(AuthFilter authFilter) {
        return route("employee-service")
                .route(path("/api/employees/**"), http()) // http() now takes NO arguments
                .before(uri(URI.create("http://localhost:8081"))) // This sets the target
                .filter(authFilter.validate())
                .build()
                .and(route("iam-service")
                        .route(path("/auth/**"), http()) // http() now takes NO arguments
                        .before(uri(URI.create("http://localhost:7000"))) // This sets the target
                        .build());
    }
}