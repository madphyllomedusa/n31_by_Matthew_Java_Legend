package ru.spb.n31.n31_by_matthew_java_legend.dto.response;

public record SubserviceResponse(
        String subserviceId,
        String title,
        String description,
        String workHours,
        Integer averagePrice,
        java.util.List<SubserviceTypeResponse> types
) {
}
