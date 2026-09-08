package com.vicentedev.api_re.dto.response;

import com.vicentedev.api_re.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados de representação de um usuário")
public record UserResponse(
        @Schema(description = "Identificador único do usuário", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
        UUID id,

        @Schema(description = "Nome do usuário", example = "Carlos Silva")
        String name,

        @Schema(description = "E-mail cadastrado", example = "carlos.silva@email.com")
        String email,

        @Schema(description = "Perfil de acesso / cargo (Role)", example = "VISITOR")
        Role role,

        @Schema(description = "Data e hora do último login realizado", example = "2026-09-07T10:30:00Z")
        OffsetDateTime lastLoginAt,

        @Schema(description = "Data de criação da conta", example = "2026-09-07T08:00:00Z")
        OffsetDateTime createdAt,

        @Schema(description = "Data da última modificação", example = "2026-09-07T08:00:00Z")
        OffsetDateTime updatedAt
) {
}
