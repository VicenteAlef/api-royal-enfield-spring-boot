package com.vicentedev.api_re.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de autenticação bem-sucedida contendo o token JWT")
public record AuthResponse(
        @Schema(description = "Token de acesso JWT emitido", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Tipo do token", example = "Bearer")
        String tokenType,

        @Schema(description = "Tempo de expiração do token em milissegundos", example = "86400000")
        long expiresIn,

        @Schema(description = "Dados do usuário autenticado")
        UserResponse user
) {
}
