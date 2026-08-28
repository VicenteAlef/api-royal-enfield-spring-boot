package com.vicentedev.api_re.service;

import com.vicentedev.api_re.dto.request.MotorcycleGalleryRequest;
import com.vicentedev.api_re.dto.response.MotorcycleGalleryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MotorcycleGalleryService {

    MotorcycleGalleryResponse addImage(UUID motorcycleId, MotorcycleGalleryRequest request, MultipartFile file);

    List<MotorcycleGalleryResponse> getByMotorcycleId(UUID motorcycleId);

    MotorcycleGalleryResponse updateImage(UUID id, MotorcycleGalleryRequest request, MultipartFile file);

    void deleteImage(UUID id);
}
