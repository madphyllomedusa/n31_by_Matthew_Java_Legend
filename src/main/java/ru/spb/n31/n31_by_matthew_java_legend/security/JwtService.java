package ru.spb.n31.n31_by_matthew_java_legend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.spb.n31.n31_by_matthew_java_legend.exception.BadRequestException;
import ru.spb.n31.n31_by_matthew_java_legend.util.JwtProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Duration expiration = jwtProperties.expiration();
        if (expiration == null || expiration.isZero() || expiration.isNegative()) {
            throw new BadRequestException("JWT expiration is not configured or invalid. Set jwt.expiration (e.g. 15m, 24h)");
        }
        Date expiresAt = new Date(now.getTime() + expiration.toMillis());

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .claim("roles", roles)
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parseAllClaims(token).getSubject();
    }

    public Collection<String> extractRoles(String token) {
        Claims claims = parseAllClaims(token);
        Object roles = claims.get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = parseAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey signingKey() {
        String secret = jwtProperties.secret();
        if (secret == null || secret.isBlank()) {
            throw new BadRequestException("JWT secret is not configured. Set jwt.secret (or JWT_SECRET env var)");
        }
        // HS256 требует минимум 256 бит (32 байта)
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new BadRequestException("JWT secret is too short. Provide at least 32 bytes for HS256");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


