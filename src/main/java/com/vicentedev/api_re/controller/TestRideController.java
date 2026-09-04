package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.TestRideCreateRequest;
import com.vicentedev.api_re.dto.request.TestRideStatusUpdateRequest;
import com.vicentedev.api_re.dto.response.TestRideResponse;
import com.vicentedev.api_re.entity.TestRideStatus;
import com.vicentedev.api_re.service.TestRideService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1/test-rides")
public class TestRideController {

    private final TestRideService testRideService;

    public TestRideController(TestRideService testRideService) {
        this.testRideService = testRideService;
    }

    @PostMapping
    public ResponseEntity<TestRideResponse> create(@Valid @RequestBody TestRideCreateRequest request) {
        TestRideResponse response = testRideService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TestRideResponse>> list(
            @RequestParam(required = false) UUID dealershipId,
            @RequestParam(required = false) UUID motorcycleId,
            @RequestParam(required = false) TestRideStatus status,
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @PageableDefault(size = 10, sort = "preferredDate", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<TestRideResponse> response = testRideService.list(
                dealershipId, motorcycleId, status, customerEmail, startDate, endDate, pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestRideResponse> getById(@PathVariable UUID id) {
        TestRideResponse response = testRideService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TestRideResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody TestRideStatusUpdateRequest request
    ) {
        TestRideResponse response = testRideService.updateStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TestRideResponse> cancel(@PathVariable UUID id) {
        TestRideResponse response = testRideService.cancel(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        testRideService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
