package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.MotorcycleGalleryRequest;
import com.vicentedev.api_re.dto.response.MotorcycleGalleryResponse;
import com.vicentedev.api_re.service.MotorcycleGalleryService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "6. Galeria de Fotos", description = "Endpoints para gerenciamento do acervo de imagens e carrossel de fotos das motocicletas.")
@RestController
@RequestMapping("/api/v1")
public class MotorcycleGalleryController {

    private final MotorcycleGalleryService galleryService;

    public MotorcycleGalleryController(MotorcycleGalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @Operation(summary = "Adicionar imagem na galeria via upload físico", description = "Faz upload de um arquivo de foto para a galeria da motocicleta, salvando localmente. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imagem adicionada à galeria com sucesso."),
            @ApiResponse(responseCode = "400", description = "Formato de arquivo inválido."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PostMapping(value = "/motorcycles/{motorcycleId}/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MotorcycleGalleryResponse> addImageFile(
            @Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId,
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Legenda da imagem") @RequestParam(value = "caption", required = false) String caption,
            @Parameter(description = "Ordem de exibição no carrossel") @RequestParam(value = "displayOrder", required = false, defaultValue = "0") Integer displayOrder
    ) {
        MotorcycleGalleryRequest request = new MotorcycleGalleryRequest(null, caption, displayOrder);
        MotorcycleGalleryResponse response = galleryService.addImage(motorcycleId, request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Adicionar imagem na galeria via URL externa", description = "Registra uma URL de imagem externa para a galeria da motocicleta. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imagem registrada na galeria com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PostMapping(value = "/motorcycles/{motorcycleId}/gallery/url", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MotorcycleGalleryResponse> addImageUrl(
            @Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId,
            @Valid @RequestBody MotorcycleGalleryRequest request
    ) {
        MotorcycleGalleryResponse response = galleryService.addImage(motorcycleId, request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar galeria de fotos de uma motocicleta", description = "Retorna todas as imagens cadastradas para a moto, ordenadas pelo campo displayOrder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Galeria de fotos retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Motocicleta não encontrada.")
    })
    @GetMapping("/motorcycles/{motorcycleId}/gallery")
    public ResponseEntity<List<MotorcycleGalleryResponse>> listGallery(@Parameter(description = "UUID da motocicleta") @PathVariable UUID motorcycleId) {
        List<MotorcycleGalleryResponse> response = galleryService.getByMotorcycleId(motorcycleId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar imagem ou legenda da galeria", description = "Atualiza a legenda, ordem ou substitui a foto física da galeria. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item da galeria atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Item da galeria não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @PutMapping(value = "/gallery/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MotorcycleGalleryResponse> updateImage(
            @Parameter(description = "UUID do item da galeria") @PathVariable UUID id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @Parameter(description = "Nova legenda") @RequestParam(value = "caption", required = false) String caption,
            @Parameter(description = "Nova ordem de exibição") @RequestParam(value = "displayOrder", required = false) Integer displayOrder
    ) {
        MotorcycleGalleryRequest request = new MotorcycleGalleryRequest(null, caption, displayOrder);
        MotorcycleGalleryResponse response = galleryService.updateImage(id, request, file);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remover imagem da galeria", description = "Exclui a foto da galeria e remove o arquivo físico correspondente do disco. Requer perfil USER ou ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Foto removida com sucesso."),
            @ApiResponse(responseCode = "404", description = "Item da galeria não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    @DeleteMapping("/gallery/{id}")
    public ResponseEntity<Void> deleteImage(@Parameter(description = "UUID do item da galeria") @PathVariable UUID id) {
        galleryService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
