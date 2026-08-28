package com.vicentedev.api_re.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MotorcycleUpdateRequest(
        @NotBlank(message = "Model name is required")
        @Size(max = 100, message = "Model name cannot exceed 100 characters")
        String modelName,

        @NotBlank(message = "Family is required")
        @Size(max = 50, message = "Family cannot exceed 50 characters")
        String family,

        @NotNull(message = "Engine CC is required")
        @Positive(message = "Engine CC must be greater than zero")
        Integer engineCc,

        @NotNull(message = "Starting price is required")
        @Positive(message = "Starting price must be greater than zero")
        BigDecimal startingPrice,

        String description,

        Boolean active
) {
}
