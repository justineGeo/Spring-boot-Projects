package com.jso.EmployeeService.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorProvider implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // 1. Check if the user is even logged in
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // 2. Extract identity from the JWT
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            // Check for your custom 'userId' claim first
            String userId = jwtAuth.getToken().getClaim("userId");

            // Professional Fallback: If 'userId' is null, use the standard 'sub' (subject) field
            if (userId == null) {
                userId = jwtAuth.getToken().getSubject();
            }

            return Optional.ofNullable(userId);
        }

        // 3. Fallback for non-JWT authentication (e.g., during some tests)
        return Optional.ofNullable(authentication.getName());
    }
}