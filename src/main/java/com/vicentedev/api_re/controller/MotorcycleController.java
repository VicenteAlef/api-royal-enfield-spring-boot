package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.MotorcycleCreateRequest;
import com.vicentedev.api_re.dto.request.MotorcycleUpdateRequest;
import com.vicentedev.api_re.dto.response.MotorcycleDetailResponse;
import com.vicentedev.api_re.dto.response.MotorcycleSummaryResponse;
import com.vicentedev.api_re.service.MotorcycleService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "3. Motocicletas", description = "Catálogo de modelos Royal Enfield, criação, listagem com filtros, detalhes com ficha técnica, variantes e galeria, atualização e exclusão em cascata.")
@RestController
@RequestMapping("/api/v1/motorcycles")
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    public MotorcycleController(MotorcycleService motorcycleService) {
        this.motorcycleService = motorcycleService;
    }

    @Operation(summary = "Criar nova motocicleta", description = "Cadastra um modelo base de motocicleta com ficha técnica inicial opcional. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Motocicleta criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados da motocicleta inválidos."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PostMapping
    public ResponseEntity<MotorcycleDetailResponse> create(@Valid @RequestBody MotorcycleCreateRequest request) {
        MotorcycleDetailResponse created = motorcycleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Listar motocicletas (Paginado com Filtros)", description = "Retorna o catálogo público de motocicletas com paginação e filtros opcionais por família, status ativo e busca textual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catálogo de motocicletas retornado com sucesso.")
    })
    @GetMapping
    public ResponseEntity<Page<MotorcycleSummaryResponse>> list(
            @Parameter(description = "Filtrar por família (ex: Cruiser, Classic, Roadster, Adventure)") @RequestParam(required = false) String family,
            @Parameter(description = "Filtrar por status ativo (true/false)") @RequestParam(required = false) Boolean active,
            @Parameter(description = "Busca textual no nome do modelo") @RequestParam(required = false) String query,
            @ParameterObject @PageableDefault(size = 10, sort = "modelName", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<MotorcycleSummaryResponse> result = motorcycleService.list(family, active, query, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Obter detalhes da motocicleta por ID", description = "Retorna as informações completas da moto incluindo ficha técnica, lista de variantes/cores e galeria de fotos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes da motocicleta recuperados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MotorcycleDetailResponse> getById(@Parameter(description = "UUID da motocicleta") @PathVariable UUID id) {
        MotorcycleDetailResponse detail = motorcycleService.getById(id);
        return ResponseEntity.ok(detail);
    }

    @Operation(summary = "Atualizar dados da motocicleta", description = "Atualiza dados cadastrais básicos de um modelo. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Motocicleta atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de atualização inválidos."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MotorcycleDetailResponse> update(
            @Parameter(description = "UUID da motocicleta") @PathVariable UUID id,
            @Valid @RequestBody MotorcycleUpdateRequest request
    ) {
        MotorcycleDetailResponse updated = motorcycleService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Ativar / Desativar motocicleta", description = "Alterna o status ativo/inativo da motocicleta no catálogo. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<MotorcycleSummaryResponse> toggleStatus(@Parameter(description = "UUID da motocicleta") @PathVariable UUID id) {
        MotorcycleSummaryResponse updated = motorcycleService.toggleActive(id);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Excluir motocicleta", description = "Exclui a motocicleta em cascata (ficha técnica, variantes, galeria) e remove fisicamente todos os arquivos de imagem associados. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Motocicleta e mídias associadas excluídas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "UUID da motocicleta") @PathVariable UUID id) {
        motorcycleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
