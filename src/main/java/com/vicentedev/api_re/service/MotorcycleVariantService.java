package com.vicentedev.api_re.service;

import com.vicentedev.api_re.dto.request.MotorcycleVariantRequest;
import com.vicentedev.api_re.dto.response.MotorcycleVariantResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MotorcycleVariantService {

    MotorcycleVariantResponse create(UUID motorcycleId, MotorcycleVariantRequest request, MultipartFile image);

    List<MotorcycleVariantResponse> getByMotorcycleId(UUID motorcycleId, boolean activeOnly);

    MotorcycleVariantResponse getById(UUID id);

    MotorcycleVariantResponse update(UUID id, MotorcycleVariantRequest request);

    MotorcycleVariantResponse uploadImage(UUID id, MultipartFile file);

    void delete(UUID id);
}
