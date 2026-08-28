package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.MotorcycleVariantRequest;
import com.vicentedev.api_re.dto.response.MotorcycleVariantResponse;
import com.vicentedev.api_re.service.MotorcycleVariantService;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MotorcycleVariantController {

    private final MotorcycleVariantService variantService;

    public MotorcycleVariantController(MotorcycleVariantService variantService) {
        this.variantService = variantService;
    }

    @PostMapping(value = "/motorcycles/{motorcycleId}/variants", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MotorcycleVariantResponse> createJson(
            @PathVariable UUID motorcycleId,
            @Valid @RequestBody MotorcycleVariantRequest request
    ) {
        MotorcycleVariantResponse response = variantService.create(motorcycleId, request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/motorcycles/{motorcycleId}/variants", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MotorcycleVariantResponse> createMultipart(
            @PathVariable UUID motorcycleId,
            @Valid @RequestPart("data") MotorcycleVariantRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        MotorcycleVariantResponse response = variantService.create(motorcycleId, request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/motorcycles/{motorcycleId}/variants")
    public ResponseEntity<List<MotorcycleVariantResponse>> listByMotorcycle(
            @PathVariable UUID motorcycleId,
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        List<MotorcycleVariantResponse> response = variantService.getByMotorcycleId(motorcycleId, activeOnly);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/variants/{id}")
    public ResponseEntity<MotorcycleVariantResponse> getById(@PathVariable UUID id) {
        MotorcycleVariantResponse response = variantService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/variants/{id}")
    public ResponseEntity<MotorcycleVariantResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody MotorcycleVariantRequest request
    ) {
        MotorcycleVariantResponse response = variantService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/variants/{id}/image", method = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.PUT}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MotorcycleVariantResponse> uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {
        MotorcycleVariantResponse response = variantService.uploadImage(id, file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/variants/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        variantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
