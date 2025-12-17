package ru.spb.n31.n31_by_matthew_java_legend.dto.request;

public record ServiceRequest(
        String id,
        String title,
        Integer price,
        String image
) {
}