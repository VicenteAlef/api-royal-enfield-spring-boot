package com.vicentedev.api_re.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TestRideCreateRequest(
        @NotBlank(message = "Customer name is required")
        @Size(max = 100, message = "Customer name must not exceed 100 characters")
        String customerName,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Customer email must not exceed 100 characters")
        String customerEmail,

        @NotBlank(message = "Customer phone is required")
        @Size(max = 20, message = "Customer phone must not exceed 20 characters")
        String customerPhone,

        @NotNull(message = "Preferred date is required")
        @Future(message = "Preferred date must be a future date and time")
        OffsetDateTime preferredDate,

        @NotNull(message = "Motorcycle ID is required")
        UUID motorcycleId,

        UUID variantId,

        @NotNull(message = "Dealership ID is required")
        UUID dealershipId
) {
}
