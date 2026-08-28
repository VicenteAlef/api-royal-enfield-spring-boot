package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.dto.request.MotorcycleCreateRequest;
import com.vicentedev.api_re.dto.request.MotorcycleUpdateRequest;
import com.vicentedev.api_re.dto.response.MotorcycleDetailResponse;
import com.vicentedev.api_re.dto.response.MotorcycleSummaryResponse;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.exception.ResourceNotFoundException;
import com.vicentedev.api_re.mapper.MotorcycleMapper;
import com.vicentedev.api_re.repository.MotorcycleRepository;
import com.vicentedev.api_re.repository.specification.MotorcycleSpecification;
import com.vicentedev.api_re.service.FileStorageService;
import com.vicentedev.api_re.service.MotorcycleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class MotorcycleServiceImpl implements MotorcycleService {

    private final MotorcycleRepository motorcycleRepository;
    private final MotorcycleMapper motorcycleMapper;
    private final FileStorageService fileStorageService;

    public MotorcycleServiceImpl(
            MotorcycleRepository motorcycleRepository,
            MotorcycleMapper motorcycleMapper,
            FileStorageService fileStorageService
    ) {
        this.motorcycleRepository = motorcycleRepository;
        this.motorcycleMapper = motorcycleMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public MotorcycleDetailResponse create(MotorcycleCreateRequest request) {
        Motorcycle motorcycle = motorcycleMapper.toEntity(request);
        Motorcycle saved = motorcycleRepository.save(motorcycle);
        return motorcycleMapper.toDetailResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MotorcycleSummaryResponse> list(String family, Boolean active, String query, Pageable pageable) {
        Specification<Motorcycle> spec = MotorcycleSpecification.withFilters(family, active, query);
        Page<Motorcycle> motorcycles = motorcycleRepository.findAll(spec, pageable);
        return motorcycles.map(motorcycleMapper::toSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MotorcycleDetailResponse getById(UUID id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + id));
        return motorcycleMapper.toDetailResponse(motorcycle);
    }

    @Override
    public MotorcycleDetailResponse update(UUID id, MotorcycleUpdateRequest request) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + id));

        motorcycleMapper.updateEntityFromRequest(request, motorcycle);
        Motorcycle saved = motorcycleRepository.save(motorcycle);
        return motorcycleMapper.toDetailResponse(saved);
    }

    @Override
    public MotorcycleSummaryResponse toggleActive(UUID id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + id));

        motorcycle.setActive(!Boolean.TRUE.equals(motorcycle.getActive()));
        Motorcycle saved = motorcycleRepository.save(motorcycle);
        return motorcycleMapper.toSummaryResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + id));

        if (motorcycle.getVariants() != null) {
            motorcycle.getVariants().forEach(variant -> {
                if (variant.getImageUrl() != null) {
                    fileStorageService.deleteFile(variant.getImageUrl());
                }
            });
        }

        if (motorcycle.getGallery() != null) {
            motorcycle.getGallery().forEach(galleryItem -> {
                if (galleryItem.getImageUrl() != null) {
                    fileStorageService.deleteFile(galleryItem.getImageUrl());
                }
            });
        }

        motorcycleRepository.delete(motorcycle);
    }
}
