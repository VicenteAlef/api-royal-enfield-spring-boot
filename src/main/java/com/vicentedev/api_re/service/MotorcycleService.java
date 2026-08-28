package com.vicentedev.api_re.service;

import com.vicentedev.api_re.dto.request.MotorcycleCreateRequest;
import com.vicentedev.api_re.dto.request.MotorcycleUpdateRequest;
import com.vicentedev.api_re.dto.response.MotorcycleDetailResponse;
import com.vicentedev.api_re.dto.response.MotorcycleSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MotorcycleService {

    MotorcycleDetailResponse create(MotorcycleCreateRequest request);

    Page<MotorcycleSummaryResponse> list(String family, Boolean active, String query, Pageable pageable);

    MotorcycleDetailResponse getById(UUID id);

    MotorcycleDetailResponse update(UUID id, MotorcycleUpdateRequest request);

    MotorcycleSummaryResponse toggleActive(UUID id);

    void delete(UUID id);
}
