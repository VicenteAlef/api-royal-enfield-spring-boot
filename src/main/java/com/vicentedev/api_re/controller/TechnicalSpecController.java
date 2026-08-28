package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.TechnicalSpecRequest;
import com.vicentedev.api_re.dto.response.TechnicalSpecResponse;
import com.vicentedev.api_re.service.TechnicalSpecService;
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

@RestController
@RequestMapping("/api/v1/motorcycles/{motorcycleId}/technical-spec")
public class TechnicalSpecController {

    private final TechnicalSpecService technicalSpecService;

    public TechnicalSpecController(TechnicalSpecService technicalSpecService) {
        this.technicalSpecService = technicalSpecService;
    }

    @GetMapping
    public ResponseEntity<TechnicalSpecResponse> getByMotorcycleId(@PathVariable UUID motorcycleId) {
        TechnicalSpecResponse response = technicalSpecService.getByMotorcycleId(motorcycleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<TechnicalSpecResponse> createOrUpdate(
            @PathVariable UUID motorcycleId,
            @Valid @RequestBody TechnicalSpecRequest request
    ) {
        TechnicalSpecResponse response = technicalSpecService.createOrUpdate(motorcycleId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable UUID motorcycleId) {
        technicalSpecService.delete(motorcycleId);
        return ResponseEntity.noContent().build();
    }
}
