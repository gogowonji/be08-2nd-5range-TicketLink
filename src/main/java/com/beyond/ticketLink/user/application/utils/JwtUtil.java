package com.beyond.ticketLink.user.application.utils;


import com.beyond.ticketLink.user.application.domain.TicketLinkUserDetails;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    public final long accessTokenExpTime;
    public final long refreshTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-exp-time}") long accessTokenExpTime,
            @Value("${jwt.refresh-token-exp-time}") long refreshTokenExpTime
    ) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );

        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public String getUserNo(String token) {
        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userNo", String.class);
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String createAccessToken(TicketLinkUserDetails member) {
        HashMap<String, String> claims = new HashMap<>();

        claims.put("userNo", member.getUserNo());
        claims.put("username", member.getUsername());
        claims.put("role", member.getRole());

        return createJwt(claims, accessTokenExpTime);

    }

    public String createRefreshToken(TicketLinkUserDetails member) {
        HashMap<String, String> claims = new HashMap<>();

        claims.put("username", member.getUsername());

        return createJwt(claims, accessTokenExpTime);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    private String createJwt(Map<String, String> claims, Long expiredMs) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

}