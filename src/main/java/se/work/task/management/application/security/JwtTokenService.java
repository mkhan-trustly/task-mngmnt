package se.work.task.management.application.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "FLhAiNizyU22vrl9IU1D25h7GTVYvZQDlCI+Ny89fCg=";

    private SecretKey getSigningKey() {
        byte[] decodedSecretKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedSecretKey);
    }

    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(getSigningKey()).build();
    }

    public Jwt decodeJwt(String token) {
        return jwtDecoder().decode(token);
    }
}