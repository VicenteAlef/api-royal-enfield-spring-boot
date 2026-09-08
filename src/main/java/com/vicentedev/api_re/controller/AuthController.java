package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.LoginRequest;
import com.vicentedev.api_re.dto.request.RegisterRequest;
import com.vicentedev.api_re.dto.request.Verify2FaRequest;
import com.vicentedev.api_re.dto.response.AuthResponse;
import com.vicentedev.api_re.dto.response.LoginStepResponse;
import com.vicentedev.api_re.dto.response.UserResponse;
import com.vicentedev.api_re.entity.User;
import com.vicentedev.api_re.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1. Autenticação e 2FA", description = "Endpoints para registro de usuários, login em duas etapas (2FA via e-mail), validação de OTP e emissão de JWT.")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário na plataforma com role padrão VISITOR e dispara e-mail de notificação para administradores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou e-mail já cadastrado.")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Iniciar fluxo de login (1º Fator)", description = "Valida credenciais (e-mail e senha) e despacha um código OTP de 6 dígitos para o e-mail cadastrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciais válidas e código 2FA enviado por e-mail."),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha incorretos.")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginStepResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginStepResponse response = authService.initiateLogin(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Validar código 2FA e emitir JWT (2º Fator)", description = "Valida o código OTP de 6 dígitos enviado por e-mail e emite o token JWT para autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação concluída com sucesso e token JWT emitido."),
            @ApiResponse(responseCode = "400", description = "Código 2FA expirado, inválido ou já utilizado.")
    })
    @PostMapping("/verify-2fa")
    public ResponseEntity<AuthResponse> verify2Fa(@Valid @RequestBody Verify2FaRequest request) {
        AuthResponse response = authService.verify2Fa(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obter dados do usuário autenticado", description = "Retorna os detalhes e permissões do usuário logado baseado no token JWT informado no cabeçalho.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil do usuário recuperado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado / token ausente ou inválido.")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        UserResponse response = authService.getCurrentUser(user);
        return ResponseEntity.ok(response);
    }
}
