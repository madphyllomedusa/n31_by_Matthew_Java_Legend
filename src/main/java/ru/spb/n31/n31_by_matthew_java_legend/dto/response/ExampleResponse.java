package ru.spb.n31.n31_by_matthew_java_legend.dto.response;

public record ExampleResponse(
        String id,
        String typeId,
        String title,
        String description,
        Integer price,
        String image
) {
}
