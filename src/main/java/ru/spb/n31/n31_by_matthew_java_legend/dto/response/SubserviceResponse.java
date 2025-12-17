package ru.spb.n31.n31_by_matthew_java_legend.dto.response;

public record SubserviceResponse(
        String subserviceId,
        java.util.List<SubserviceTypeResponse> types
) {
}