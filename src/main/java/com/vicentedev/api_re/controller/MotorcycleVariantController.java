package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.MotorcycleVariantRequest;
import com.vicentedev.api_re.dto.response.MotorcycleVariantResponse;
import com.vicentedev.api_re.service.MotorcycleVariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "5. Variantes e Cores", description = "Endpoints para gerenciamento de variantes de modelos, cores, preços e uploads de fotos.")
@RestController
@RequestMapping("/api/v1")
public class MotorcycleVariantController {

    private final MotorcycleVariantService variantService;

    public MotorcycleVariantController(MotorcycleVariantService variantService) {
        this.variantService = variantService;
    }

    @Operation(summary = "Criar variante via JSON", description = "Cadastra uma variante (versão/cor) para uma motocicleta sem envio inicial de arquivo de imagem. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Variante criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados da variante inválidos."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PostMapping(value = "/motorcycles/{motorcycleId}/variants", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MotorcycleVariantResponse> createJson(
            @Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId,
            @Valid @RequestBody MotorcycleVariantRequest request
    ) {
        MotorcycleVariantResponse response = variantService.create(motorcycleId, request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Criar variante com upload de imagem", description = "Cadastra uma variante enviando os dados em JSON (part 'data') e o arquivo de imagem (part 'image'). Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Variante criada e imagem armazenada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados ou formato de imagem inválido (permitido: jpg, jpeg, png, webp)."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PostMapping(value = "/motorcycles/{motorcycleId}/variants", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MotorcycleVariantResponse> createMultipart(
            @Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId,
            @Valid @RequestPart("data") MotorcycleVariantRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        MotorcycleVariantResponse response = variantService.create(motorcycleId, request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar variantes de uma motocicleta", description = "Retorna as variantes e opções de cores de um determinado modelo de moto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de variantes retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada.")
    })
    @GetMapping("/motorcycles/{motorcycleId}/variants")
    public ResponseEntity<List<MotorcycleVariantResponse>> listByMotorcycle(
            @Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId,
            @Parameter(description = "Filtrar apenas variantes ativas") @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        List<MotorcycleVariantResponse> response = variantService.getByMotorcycleId(motorcycleId, activeOnly);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obter variante por ID", description = "Retorna os detalhes de uma variante a partir do seu identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variante encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Variante não encontrada.")
    })
    @GetMapping("/variants/{id}")
    public ResponseEntity<MotorcycleVariantResponse> getById(@Parameter(description = "UUID da variante") @PathVariable UUID id) {
        MotorcycleVariantResponse response = variantService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar dados da variante", description = "Atualiza os dados de uma variante existente. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variante atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados da variante inválidos."),
            @ApiResponse(responseCode = "404", description = "Variante não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PutMapping("/variants/{id}")
    public ResponseEntity<MotorcycleVariantResponse> update(
            @Parameter(description = "UUID da variante") @PathVariable UUID id,
            @Valid @RequestBody MotorcycleVariantRequest request
    ) {
        MotorcycleVariantResponse response = variantService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Fazer upload ou substituir imagem da variante", description = "Envia uma nova foto para a variante, excluindo o arquivo físico antigo do disco local caso exista. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem enviada e URL atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou extensão não suportada."),
            @ApiResponse(responseCode = "404", description = "Variante não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @RequestMapping(value = "/variants/{id}/image", method = {RequestMethod.POST, RequestMethod.PUT}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MotorcycleVariantResponse> uploadImage(
            @Parameter(description = "UUID da variante") @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {
        MotorcycleVariantResponse response = variantService.uploadImage(id, file);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir variante", description = "Remove a variante do banco de dados e apaga o arquivo físico de imagem do disco local. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Variante excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Variante não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @DeleteMapping("/variants/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "UUID da variante") @PathVariable UUID id) {
        variantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
