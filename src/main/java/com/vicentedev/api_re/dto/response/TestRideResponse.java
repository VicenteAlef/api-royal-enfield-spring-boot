package com.vicentedev.api_re.dto.response;

import com.vicentedev.api_re.entity.TestRideStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TestRideResponse(
        UUID id,
        String customerName,
        String customerEmail,
        String customerPhone,
        OffsetDateTime preferredDate,
        TestRideStatus status,
        MotorcycleSummary motorcycle,
        VariantSummary variant,
        DealershipResponse dealership,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public record MotorcycleSummary(
            UUID id,
            String modelName,
            String family,
            Integer engineCc
    ) {
    }

    public record VariantSummary(
            UUID id,
            String variantName,
            String colorName,
            String imageUrl
    ) {
    }
}
