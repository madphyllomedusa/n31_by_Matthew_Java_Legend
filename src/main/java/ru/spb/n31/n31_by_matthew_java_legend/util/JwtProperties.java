package ru.spb.n31.n31_by_matthew_java_legend.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Настройки JWT.
 * <p>
 * {@code jwt.expiration} — время жизни токена в формате {@link Duration} (например: {@code 15m}, {@code 24h}).
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Duration expiration
) {
}
