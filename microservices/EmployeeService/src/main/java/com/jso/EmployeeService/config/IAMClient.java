//package com.jso.EmployeeService.config;
//
//import com.jso.EmployeeService.Dtos.UserResponse;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.stereotype.Component;
//
//@Component
//public class IAMClient {
//    private final WebClient webClient;
//
//    public IAMClient(WebClient.Builder builder) {
//        this.webClient = builder.baseUrl("http://iam-service/api/v1/auth").build();
//    }
//
//    public UserResponse fetchUserById(Long userId, String token) {
//        return webClient.get()
//                .uri("/users/{id}", userId)
//                .header("Authorization", "Bearer " + token)
//                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, response ->
//                        Mono.error(new RuntimeException("User not found in IAM service")))
//                .bodyToMono(UserResponse.class)
//                .block();
//    }
//}