package ru.spb.n31.n31_by_matthew_java_legend.dto.request;

public record SubserviceRequest(
        String subserviceId,
        java.util.List<SubserviceTypeRequest> types
) {
}