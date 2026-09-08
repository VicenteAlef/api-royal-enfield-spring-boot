package com.vicentedev.api_re.dto.request;

import com.vicentedev.api_re.entity.TestRideStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualização de status do agendamento de test ride")
public record TestRideStatusUpdateRequest(
        @Schema(description = "Novo status do agendamento", example = "CONFIRMED")
        @NotNull(message = "Status is required")
        TestRideStatus status
) {
}
