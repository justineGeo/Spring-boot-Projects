package com.jso.EmployeeService.Services;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class IamIntegrationService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    // Use the Gateway URL or direct IAM URL (Port 7000 as per your config)
    private final String IAM_URL = "http://localhost:7000/auth/organizations/validate/";

    public boolean isOrganizationValid(Long orgId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(IAM_URL + orgId))
                    .GET()
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            // If IAM returns 200 OK, the organization exists
            return response.statusCode() == 200;
        } catch (Exception e) {
            // Log error: IAM service might be down
            return false;
        }
    }
}