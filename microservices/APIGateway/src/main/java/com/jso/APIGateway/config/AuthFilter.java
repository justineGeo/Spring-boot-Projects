package com.jso.APIGateway.config;

import com.jso.APIGateway.config.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class AuthFilter {

    private final JwtService jwtService;

    public AuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> validate() {
        return (request, next) -> {
            var authHeader = request.headers().header("Authorization").stream().findFirst();

            if (authHeader.isEmpty() || !authHeader.get().startsWith("Bearer ")) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
            }
            try {
                String token = authHeader.get().substring(7);
                Claims claims = jwtService.extractAllClaims(token);

                // Pass user information forward to the microservice
                var mutatedRequest = org.springframework.web.servlet.function.ServerRequest.from(request)
                        .header("X-User-Id", claims.getSubject())
                        .header("X-User-Roles", claims.get("roles", String.class))
                        .build();

                return next.handle(mutatedRequest);
            } catch (Exception e) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
            }
        };
    }
}