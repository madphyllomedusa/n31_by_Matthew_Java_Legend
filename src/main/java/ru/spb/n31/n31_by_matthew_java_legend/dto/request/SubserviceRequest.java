package ru.spb.n31.n31_by_matthew_java_legend.dto.request;

public record SubserviceRequest(
        String subserviceId,
        String title,
        String description,
        String workHours,
        Integer averagePrice,
        java.util.List<SubserviceTypeRequest> types
) {
}
