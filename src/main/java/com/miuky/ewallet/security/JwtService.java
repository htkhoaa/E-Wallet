package com.miuky.ewallet.security;

import com.miuky.ewallet.exception.AppException;
import com.miuky.ewallet.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token.expiration}")
    private long jwtTokenExpiration;
    @Value("${jwt.issuer}")
    private String issuer;

    public String generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtTokenExpiration);

        Map<String, Object> claims = Map.of(
                "phoneNumber", userDetails.getUsername(),
                "kycStatus", userDetails.getKycStatus(),
                "role", userDetails.getAuthorities(),
                "id", userDetails.getId()
        );

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ex) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }


    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

}
