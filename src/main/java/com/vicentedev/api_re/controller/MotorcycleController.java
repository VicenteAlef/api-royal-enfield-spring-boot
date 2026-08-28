package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.MotorcycleCreateRequest;
import com.vicentedev.api_re.dto.request.MotorcycleUpdateRequest;
import com.vicentedev.api_re.dto.response.MotorcycleDetailResponse;
import com.vicentedev.api_re.dto.response.MotorcycleSummaryResponse;
import com.vicentedev.api_re.service.MotorcycleService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1/motorcycles")
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    public MotorcycleController(MotorcycleService motorcycleService) {
        this.motorcycleService = motorcycleService;
    }

    @PostMapping
    public ResponseEntity<MotorcycleDetailResponse> create(@Valid @RequestBody MotorcycleCreateRequest request) {
        MotorcycleDetailResponse created = motorcycleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<MotorcycleSummaryResponse>> list(
            @RequestParam(required = false) String family,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String query,
            @PageableDefault(size = 10, sort = "modelName", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<MotorcycleSummaryResponse> result = motorcycleService.list(family, active, query, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotorcycleDetailResponse> getById(@PathVariable UUID id) {
        MotorcycleDetailResponse detail = motorcycleService.getById(id);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotorcycleDetailResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody MotorcycleUpdateRequest request
    ) {
        MotorcycleDetailResponse updated = motorcycleService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<MotorcycleSummaryResponse> toggleStatus(@PathVariable UUID id) {
        MotorcycleSummaryResponse updated = motorcycleService.toggleActive(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        motorcycleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
