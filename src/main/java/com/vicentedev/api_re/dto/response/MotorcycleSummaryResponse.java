package com.vicentedev.api_re.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MotorcycleSummaryResponse(
        UUID id,
        String modelName,
        String family,
        Integer engineCc,
        BigDecimal startingPrice,
        String description,
        Boolean active,
        String mainImageUrl,
        Integer variantCount,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
