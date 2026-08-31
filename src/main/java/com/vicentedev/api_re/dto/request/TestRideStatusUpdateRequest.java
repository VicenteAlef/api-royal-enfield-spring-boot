package com.vicentedev.api_re.dto.request;

import com.vicentedev.api_re.entity.TestRideStatus;
import jakarta.validation.constraints.NotNull;

public record TestRideStatusUpdateRequest(
        @NotNull(message = "Status is required")
        TestRideStatus status
) {
}
