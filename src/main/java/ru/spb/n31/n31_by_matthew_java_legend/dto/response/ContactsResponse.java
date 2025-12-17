package ru.spb.n31.n31_by_matthew_java_legend.dto.response;

import java.util.List;

public record ContactsResponse(
        String description,
        List<ContactResponse> contacts
) {
}
