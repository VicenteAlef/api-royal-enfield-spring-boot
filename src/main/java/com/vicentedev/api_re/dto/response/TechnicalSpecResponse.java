package com.vicentedev.api_re.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TechnicalSpecResponse(
        UUID id,
        String powerHp,
        String torqueNm,
        BigDecimal weightKg,
        BigDecimal fuelCapacityL,
        Integer seatHeightMm,
        String transmission,
        String frontBrake,
        String rearBrake,
        String coolingSystem,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
