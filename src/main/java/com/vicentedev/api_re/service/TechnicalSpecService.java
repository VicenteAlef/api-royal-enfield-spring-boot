package com.vicentedev.api_re.service;

import com.vicentedev.api_re.dto.request.TechnicalSpecRequest;
import com.vicentedev.api_re.dto.response.TechnicalSpecResponse;

import java.util.UUID;

public interface TechnicalSpecService {

    TechnicalSpecResponse getByMotorcycleId(UUID motorcycleId);

    TechnicalSpecResponse createOrUpdate(UUID motorcycleId, TechnicalSpecRequest request);

    void delete(UUID motorcycleId);
}
