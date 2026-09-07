package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.TestRideCreateRequest;
import com.vicentedev.api_re.dto.request.TestRideStatusUpdateRequest;
import com.vicentedev.api_re.dto.response.TestRideResponse;
import com.vicentedev.api_re.entity.TestRideStatus;
import com.vicentedev.api_re.service.TestRideService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@Tag(name = "8. Agendamento de Test Rides", description = "Endpoints para solicitação pública, consulta com filtros, atualização de status e cancelamento de test rides.")
@RestController
@RequestMapping("/api/v1/test-rides")
public class TestRideController {

    private final TestRideService testRideService;

    public TestRideController(TestRideService testRideService) {
        this.testRideService = testRideService;
    }

    @Operation(summary = "Solicitar agendamento de test ride", description = "Endpoint público para solicitação de agendamento de test ride com validação de data futura e integridade da concessionária e moto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento registrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados do agendamento inválidos ou data no passado."),
            @ApiResponse(responseCode = "404", description = "Motocicleta, variante ou concessionária não encontrada.")
    })
    @PostMapping
    public ResponseEntity<TestRideResponse> create(@Valid @RequestBody TestRideCreateRequest request) {
        TestRideResponse response = testRideService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar agendamentos de test ride (Paginado com Filtros)", description = "Retorna lista de agendamentos com filtros opcionais por concessionária, moto, status, e-mail do cliente e intervalo de datas. Requer autenticação (VISITOR, USER, ADMIN).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado.")
    })
    @GetMapping
    public ResponseEntity<Page<TestRideResponse>> list(
            @Parameter(description = "UUID da concessionária") @RequestParam(required = false) UUID dealershipId,
            @Parameter(description = "UUID da motocicleta") @RequestParam(required = false) UUID motorcycleId,
            @Parameter(description = "Status do agendamento (PENDING, CONFIRMED, COMPLETED, CANCELLED)") @RequestParam(required = false) TestRideStatus status,
            @Parameter(description = "E-mail do cliente") @RequestParam(required = false) String customerEmail,
            @Parameter(description = "Data de início (ISO-8601)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @Parameter(description = "Data de fim (ISO-8601)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @ParameterObject @PageableDefault(size = 10, sort = "preferredDate", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<TestRideResponse> response = testRideService.list(
                dealershipId, motorcycleId, status, customerEmail, startDate, endDate, pageable
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obter agendamento por ID", description = "Retorna dados completos de um agendamento de test ride. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TestRideResponse> getById(@Parameter(description = "UUID do agendamento") @PathVariable UUID id) {
        TestRideResponse response = testRideService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar status do agendamento", description = "Permite alterar o status do agendamento (PENDING, CONFIRMED, COMPLETED, CANCELLED). Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Status inválido."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<TestRideResponse> updateStatus(
            @Parameter(description = "UUID do agendamento") @PathVariable UUID id,
            @Valid @RequestBody TestRideStatusUpdateRequest request
    ) {
        TestRideResponse response = testRideService.updateStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancelar agendamento de test ride", description = "Cancela o agendamento alterando o status para CANCELLED. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TestRideResponse> cancel(@Parameter(description = "UUID do agendamento") @PathVariable UUID id) {
        TestRideResponse response = testRideService.cancel(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir agendamento de test ride", description = "Remove um agendamento do banco de dados. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agendamento removido com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "UUID do agendamento") @PathVariable UUID id) {
        testRideService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
