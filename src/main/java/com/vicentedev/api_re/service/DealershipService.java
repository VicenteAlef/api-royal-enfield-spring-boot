package com.vicentedev.api_re.service;

import com.vicentedev.api_re.dto.request.DealershipCreateRequest;
import com.vicentedev.api_re.dto.request.DealershipUpdateRequest;
import com.vicentedev.api_re.dto.response.DealershipResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DealershipService {

    DealershipResponse create(DealershipCreateRequest request);

    Page<DealershipResponse> list(String state, String city, String query, Pageable pageable);

    DealershipResponse getById(UUID id);

    DealershipResponse update(UUID id, DealershipUpdateRequest request);

    void delete(UUID id);
}
