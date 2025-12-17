package ru.spb.n31.n31_by_matthew_java_legend.dto.request;

import java.util.List;

public record ServiceTypesProjectRequest(
        String typeId,
        List<ExampleRequest> examples
) {
}
