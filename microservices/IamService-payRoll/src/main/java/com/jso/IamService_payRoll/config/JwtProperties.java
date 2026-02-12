package com.jso.IamService_payRoll.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    // These match the 'private-key' and 'public-key' fields in your YAML
    private String privateKey;
    private String publicKey;

    private long expiration;
    private long refreshExpiration;

    @PostConstruct
    public void validateProperties() {
        if (privateKey == null || privateKey.isBlank()) {
            throw new IllegalStateException("JWT Private Key is missing! IAM Service needs this to SIGN tokens.");
        }

        if (publicKey == null || publicKey.isBlank()) {
            throw new IllegalStateException("JWT Public Key is missing! IAM Service needs this to VERIFY tokens.");
        }

        // RSA Keys are much longer than simple secrets.
        // We check if it contains the standard PEM header to ensure it's the right format.
        if (!privateKey.contains("BEGIN PRIVATE KEY")) {
            throw new IllegalStateException("JWT Private Key format is invalid. Ensure it is a PEM formatted PKCS#8 key.");
        }
    }
}