package com.gnomeshift.tisk.auth;

import com.gnomeshift.tisk.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private static final String CLAIM_TYPE = "typ";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    private final JwtProperties jwtProperties;
    private Key signingKey;
    private JwtParser jwtParser;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private JwtParser getJwtParser() {
        if (jwtParser == null) {
            jwtParser = Jwts.parser().verifyWith((SecretKey) signingKey).build();
        }
        return jwtParser;
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isAccessToken(String token) {
        return TYPE_ACCESS.equals(extractClaim(token, claims -> claims.get(CLAIM_TYPE, String.class)));
    }

    public boolean isRefreshToken(String token) {
        return TYPE_REFRESH.equals(extractClaim(token, claims -> claims.get(CLAIM_TYPE, String.class)));
    }

    public String generateAccessToken(User user) {
        return buildToken(user, jwtProperties.getAccessTokenExpiration(), TYPE_ACCESS);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, jwtProperties.getRefreshTokenExpiration(), TYPE_REFRESH);
    }

    private String buildToken(User user, long expiration, String type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_TYPE, type);
        claims.put("role", user.getRole().name());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String userId = extractId(token);

            if (userDetails instanceof User user) {
                return userId.equals(user.getId().toString()) && !isTokenExpired(token);
            }
            return false;
        }
        catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload();
    }
}
