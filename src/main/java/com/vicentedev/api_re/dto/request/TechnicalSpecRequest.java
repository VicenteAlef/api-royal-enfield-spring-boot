package com.vicentedev.api_re.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TechnicalSpecRequest(
        @Size(max = 50, message = "Power HP cannot exceed 50 characters")
        String powerHp,

        @Size(max = 50, message = "Torque NM cannot exceed 50 characters")
        String torqueNm,

        @Positive(message = "Weight must be greater than zero")
        BigDecimal weightKg,

        @Positive(message = "Fuel capacity must be greater than zero")
        BigDecimal fuelCapacityL,

        @Positive(message = "Seat height must be greater than zero")
        Integer seatHeightMm,

        @Size(max = 50, message = "Transmission cannot exceed 50 characters")
        String transmission,

        @Size(max = 100, message = "Front brake cannot exceed 100 characters")
        String frontBrake,

        @Size(max = 100, message = "Rear brake cannot exceed 100 characters")
        String rearBrake,

        @Size(max = 50, message = "Cooling system cannot exceed 50 characters")
        String coolingSystem
) {
}
