package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.DealershipCreateRequest;
import com.vicentedev.api_re.dto.request.DealershipUpdateRequest;
import com.vicentedev.api_re.dto.response.DealershipResponse;
import com.vicentedev.api_re.service.DealershipService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1/dealerships")
public class DealershipController {

    private final DealershipService dealershipService;

    public DealershipController(DealershipService dealershipService) {
        this.dealershipService = dealershipService;
    }

    @PostMapping
    public ResponseEntity<DealershipResponse> create(@Valid @RequestBody DealershipCreateRequest request) {
        DealershipResponse response = dealershipService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<DealershipResponse>> list(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String query,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<DealershipResponse> response = dealershipService.list(state, city, query, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealershipResponse> getById(@PathVariable UUID id) {
        DealershipResponse response = dealershipService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DealershipResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody DealershipUpdateRequest request
    ) {
        DealershipResponse response = dealershipService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        dealershipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
