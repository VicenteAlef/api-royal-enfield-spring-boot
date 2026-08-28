package com.vicentedev.api_re.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MotorcycleVariantRequest(
        @NotBlank(message = "Variant name is required")
        @Size(max = 100, message = "Variant name cannot exceed 100 characters")
        String variantName,

        @NotBlank(message = "Color name is required")
        @Size(max = 100, message = "Color name cannot exceed 100 characters")
        String colorName,

        @Size(max = 10, message = "Hex color code cannot exceed 10 characters")
        String hexColorCode,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @Size(max = 255, message = "Image URL cannot exceed 255 characters")
        String imageUrl,

        String includedAccessories,

        Boolean active
) {
}
