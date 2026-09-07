package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.DealershipCreateRequest;
import com.vicentedev.api_re.dto.request.DealershipUpdateRequest;
import com.vicentedev.api_re.dto.response.DealershipResponse;
import com.vicentedev.api_re.service.DealershipService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "7. Concessionárias", description = "Endpoints para consulta e gestão da rede de concessionárias autorizadas Royal Enfield.")
@RestController
@RequestMapping("/api/v1/dealerships")
public class DealershipController {

    private final DealershipService dealershipService;

    public DealershipController(DealershipService dealershipService) {
        this.dealershipService = dealershipService;
    }

    @Operation(summary = "Cadastrar concessionária", description = "Cadastra uma nova concessionária na base de dados. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Concessionária cadastrada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados da concessionária inválidos."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PostMapping
    public ResponseEntity<DealershipResponse> create(@Valid @RequestBody DealershipCreateRequest request) {
        DealershipResponse response = dealershipService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar concessionárias (Paginado com Filtros)", description = "Retorna lista paginada de concessionárias com opções de filtro por estado (UF), cidade e busca textual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de concessionárias retornada com sucesso.")
    })
    @GetMapping
    public ResponseEntity<Page<DealershipResponse>> list(
            @Parameter(description = "Sigla do estado (ex: SP, RJ, MG)") @RequestParam(required = false) String state,
            @Parameter(description = "Nome da cidade") @RequestParam(required = false) String city,
            @Parameter(description = "Busca textual por nome ou endereço") @RequestParam(required = false) String query,
            @ParameterObject @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<DealershipResponse> response = dealershipService.list(state, city, query, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obter concessionária por ID", description = "Retorna os detalhes de uma concessionária específica a partir de seu identificador UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concessionária encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Concessionária não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DealershipResponse> getById(@Parameter(description = "UUID da concessionária") @PathVariable UUID id) {
        DealershipResponse response = dealershipService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar concessionária", description = "Atualiza endereço, contatos ou nome de uma concessionária. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concessionária atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados informados inválidos."),
            @ApiResponse(responseCode = "404", description = "Concessionária não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DealershipResponse> update(
            @Parameter(description = "UUID da concessionária") @PathVariable UUID id,
            @Valid @RequestBody DealershipUpdateRequest request
    ) {
        DealershipResponse response = dealershipService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir concessionária", description = "Remove uma concessionária do sistema. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Concessionária removida com sucesso."),
            @ApiResponse(responseCode = "404", description = "Concessionária não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "UUID da concessionária") @PathVariable UUID id) {
        dealershipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
