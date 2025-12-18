package ru.spb.n31.n31_by_matthew_java_legend.dto.request;

import javax.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String login,
        @NotBlank String password
) {
}


