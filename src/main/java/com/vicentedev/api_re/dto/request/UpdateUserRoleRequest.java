package com.vicentedev.api_re.dto.request;

import com.vicentedev.api_re.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualização de permissões do usuário")
public record UpdateUserRoleRequest(
        @Schema(description = "Novo papel do usuário", example = "USER")
        @NotNull(message = "Role is required")
        Role role
) {
}
