package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.dto.request.MotorcycleVariantRequest;
import com.vicentedev.api_re.dto.response.MotorcycleVariantResponse;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.MotorcycleVariant;
import com.vicentedev.api_re.exception.ResourceNotFoundException;
import com.vicentedev.api_re.mapper.VariantMapper;
import com.vicentedev.api_re.repository.MotorcycleRepository;
import com.vicentedev.api_re.repository.MotorcycleVariantRepository;
import com.vicentedev.api_re.service.FileStorageService;
import com.vicentedev.api_re.service.MotorcycleVariantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MotorcycleVariantServiceImpl implements MotorcycleVariantService {

    private final MotorcycleVariantRepository variantRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final VariantMapper variantMapper;
    private final FileStorageService fileStorageService;

    public MotorcycleVariantServiceImpl(
            MotorcycleVariantRepository variantRepository,
            MotorcycleRepository motorcycleRepository,
            VariantMapper variantMapper,
            FileStorageService fileStorageService
    ) {
        this.variantRepository = variantRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.variantMapper = variantMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public MotorcycleVariantResponse create(UUID motorcycleId, MotorcycleVariantRequest request, MultipartFile image) {
        Motorcycle motorcycle = motorcycleRepository.findById(motorcycleId)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + motorcycleId));

        MotorcycleVariant variant = variantMapper.toEntity(request, motorcycle);

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(image, "variants");
            variant.setImageUrl(imageUrl);
        }

        MotorcycleVariant saved = variantRepository.save(variant);
        return variantMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MotorcycleVariantResponse> getByMotorcycleId(UUID motorcycleId, boolean activeOnly) {
        if (!motorcycleRepository.existsById(motorcycleId)) {
            throw new ResourceNotFoundException("Motorcycle not found with ID: " + motorcycleId);
        }

        List<MotorcycleVariant> variants = activeOnly
                ? variantRepository.findByMotorcycleIdAndActiveTrue(motorcycleId)
                : variantRepository.findByMotorcycleId(motorcycleId);

        return variantMapper.toResponseList(variants);
    }

    @Override
    @Transactional(readOnly = true)
    public MotorcycleVariantResponse getById(UUID id) {
        MotorcycleVariant variant = variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with ID: " + id));
        return variantMapper.toResponse(variant);
    }

    @Override
    public MotorcycleVariantResponse update(UUID id, MotorcycleVariantRequest request) {
        MotorcycleVariant variant = variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with ID: " + id));

        variantMapper.updateEntityFromRequest(request, variant);
        MotorcycleVariant saved = variantRepository.save(variant);
        return variantMapper.toResponse(saved);
    }

    @Override
    public MotorcycleVariantResponse uploadImage(UUID id, MultipartFile file) {
        MotorcycleVariant variant = variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with ID: " + id));

        if (variant.getImageUrl() != null && !variant.getImageUrl().isBlank()) {
            fileStorageService.deleteFile(variant.getImageUrl());
        }

        String imageUrl = fileStorageService.storeFile(file, "variants");
        variant.setImageUrl(imageUrl);
        MotorcycleVariant saved = variantRepository.save(variant);
        return variantMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        MotorcycleVariant variant = variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with ID: " + id));

        if (variant.getImageUrl() != null && !variant.getImageUrl().isBlank()) {
            fileStorageService.deleteFile(variant.getImageUrl());
        }

        variantRepository.delete(variant);
    }
}
