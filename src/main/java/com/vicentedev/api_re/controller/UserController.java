package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.UpdateUserRoleRequest;
import com.vicentedev.api_re.dto.response.UserResponse;
import com.vicentedev.api_re.entity.Role;
import com.vicentedev.api_re.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "2. Gestão de Usuários (Admin)", description = "Endpoints administrativos para listagem paginada, filtros, alteração de papéis (RBAC) e exclusão de contas.")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar usuários (Paginado com Filtros)", description = "Retorna lista paginada de usuários cadastrados com suporte a busca textual, filtro de cargo e filtro de usuários inativos (nunca acessaram). Requer ROLE_ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer perfil ADMIN).")
    })
    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(
            @Parameter(description = "Filtrar por usuários que nunca efetuaram login") @RequestParam(required = false) Boolean neverAccessed,
            @Parameter(description = "Filtrar por cargo (VISITOR, USER, ADMIN)") @RequestParam(required = false) Role role,
            @Parameter(description = "Busca textual por nome ou e-mail") @RequestParam(required = false) String query,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserResponse> response = userService.list(neverAccessed, role, query, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obter usuário por ID", description = "Retorna dados completos de um usuário a partir do seu UUID. Requer ROLE_ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer perfil ADMIN).")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@Parameter(description = "UUID do usuário") @PathVariable UUID id) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar cargo (Role) do usuário", description = "Permite alterar a role de um usuário entre VISITOR, USER e ADMIN. Requer ROLE_ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Role informada inválida."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer perfil ADMIN).")
    })
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateRole(
            @Parameter(description = "UUID do usuário") @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRoleRequest request
    ) {
        UserResponse response = userService.updateRole(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema. Requer ROLE_ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer perfil ADMIN).")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "UUID do usuário") @PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
