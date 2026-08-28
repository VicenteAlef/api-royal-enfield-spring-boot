package com.vicentedev.api_re.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record MotorcycleDetailResponse(
        UUID id,
        String modelName,
        String family,
        Integer engineCc,
        BigDecimal startingPrice,
        String description,
        Boolean active,
        TechnicalSpecResponse technicalSpec,
        List<MotorcycleVariantResponse> variants,
        List<MotorcycleGalleryResponse> gallery,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
