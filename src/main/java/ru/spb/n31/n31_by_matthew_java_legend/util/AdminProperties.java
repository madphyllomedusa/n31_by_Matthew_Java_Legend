package ru.spb.n31.n31_by_matthew_java_legend.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AdminProperties(
        String login,
        String password
) {
}
