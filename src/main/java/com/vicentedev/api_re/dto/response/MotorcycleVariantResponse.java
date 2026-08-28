package com.vicentedev.api_re.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MotorcycleVariantResponse(
        UUID id,
        UUID motorcycleId,
        String variantName,
        String colorName,
        String hexColorCode,
        BigDecimal price,
        String imageUrl,
        String includedAccessories,
        Boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
