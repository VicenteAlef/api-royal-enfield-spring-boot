package com.vicentedev.api_re.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MotorcycleGalleryResponse(
        UUID id,
        UUID motorcycleId,
        String imageUrl,
        String caption,
        Integer displayOrder,
        OffsetDateTime createdAt
) {
}
