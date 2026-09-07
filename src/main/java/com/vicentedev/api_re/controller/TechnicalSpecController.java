package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.TechnicalSpecRequest;
import com.vicentedev.api_re.dto.response.TechnicalSpecResponse;
import com.vicentedev.api_re.service.TechnicalSpecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "4. Fichas Técnicas", description = "Endpoints para gerenciamento da ficha técnica detalhada (relação 1:1 com a motocicleta).")
@RestController
@RequestMapping("/api/v1/motorcycles/{motorcycleId}/technical-spec")
public class TechnicalSpecController {

    private final TechnicalSpecService technicalSpecService;

    public TechnicalSpecController(TechnicalSpecService technicalSpecService) {
        this.technicalSpecService = technicalSpecService;
    }

    @Operation(summary = "Obter ficha técnica da motocicleta", description = "Retorna os dados de motor, potência, torque, peso, capacidade do tanque, transmissão e freios da motocicleta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ficha técnica encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Ficha técnica ou motocicleta não encontrada.")
    })
    @GetMapping
    public ResponseEntity<TechnicalSpecResponse> getByMotorcycleId(@Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId) {
        TechnicalSpecResponse response = technicalSpecService.getByMotorcycleId(motorcycleId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Criar ou atualizar ficha técnica (Upsert)", description = "Salva ou atualiza a ficha técnica vinculada à motocicleta. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ficha técnica criada ou atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados da ficha técnica inválidos."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PutMapping
    public ResponseEntity<TechnicalSpecResponse> createOrUpdate(
            @Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId,
            @Valid @RequestBody TechnicalSpecRequest request
    ) {
        TechnicalSpecResponse response = technicalSpecService.createOrUpdate(motorcycleId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remover ficha técnica", description = "Exclui o registro de ficha técnica associado à motocicleta. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ficha técnica removida com sucesso."),
            @ApiResponse(responseCode = "404", description = "Motocicleta ou ficha técnica não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @DeleteMapping
    public ResponseEntity<Void> delete(@Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId) {
        technicalSpecService.delete(motorcycleId);
        return ResponseEntity.noContent().build();
    }
}
