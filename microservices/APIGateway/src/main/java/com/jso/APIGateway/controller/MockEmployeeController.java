package com.jso.APIGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class MockEmployeeController {

    @GetMapping("/me")
    public Map<String, String> getMyInfo(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Roles") String roles) {

        // This confirms the Gateway successfully:
        // 1. Validated the JWT
        // 2. Extracted the data
        // 3. Forwarded it to us!
        return Map.of(
                "status", "Gateway Forward Successful",
                "received_user_id", userId,
                "received_roles", roles
        );
    }
}