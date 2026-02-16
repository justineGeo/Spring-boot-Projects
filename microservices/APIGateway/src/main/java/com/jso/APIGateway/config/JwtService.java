package com.jso.APIGateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private PublicKey cachedPublicKey; // Cache the parsed key

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.cachedPublicKey = initPublicKey(); // Load it once at startup
    }

    private PublicKey initPublicKey() {
        try {
            // Your logic is correct here:
            String rsaPublicKey = jwtProperties.getPublicKey()
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(rsaPublicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to load Public Key. Check your .yml configuration.", e);
        }
    }

    // Now use the cached key for validation
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(cachedPublicKey) // Fast! No more parsing strings.
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}