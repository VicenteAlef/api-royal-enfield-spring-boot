package com.vicentedev.api_re.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DealershipResponse(
        UUID id,
        String name,
        String city,
        String state,
        String address,
        String phone,
        String email,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
