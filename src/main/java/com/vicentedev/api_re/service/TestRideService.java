package com.vicentedev.api_re.service;

import com.vicentedev.api_re.dto.request.TestRideCreateRequest;
import com.vicentedev.api_re.dto.request.TestRideStatusUpdateRequest;
import com.vicentedev.api_re.dto.response.TestRideResponse;
import com.vicentedev.api_re.entity.TestRideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface TestRideService {

    TestRideResponse create(TestRideCreateRequest request);

    Page<TestRideResponse> list(
            UUID dealershipId,
            UUID motorcycleId,
            TestRideStatus status,
            String customerEmail,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            Pageable pageable
    );

    TestRideResponse getById(UUID id);

    TestRideResponse updateStatus(UUID id, TestRideStatusUpdateRequest request);

    TestRideResponse cancel(UUID id);

    void delete(UUID id);
}
