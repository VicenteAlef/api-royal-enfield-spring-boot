package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.dto.request.TechnicalSpecRequest;
import com.vicentedev.api_re.dto.response.TechnicalSpecResponse;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.TechnicalSpec;
import com.vicentedev.api_re.exception.ResourceNotFoundException;
import com.vicentedev.api_re.mapper.TechnicalSpecMapper;
import com.vicentedev.api_re.repository.MotorcycleRepository;
import com.vicentedev.api_re.repository.TechnicalSpecRepository;
import com.vicentedev.api_re.service.TechnicalSpecService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class TechnicalSpecServiceImpl implements TechnicalSpecService {

    private final TechnicalSpecRepository technicalSpecRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final TechnicalSpecMapper technicalSpecMapper;

    public TechnicalSpecServiceImpl(
            TechnicalSpecRepository technicalSpecRepository,
            MotorcycleRepository motorcycleRepository,
            TechnicalSpecMapper technicalSpecMapper
    ) {
        this.technicalSpecRepository = technicalSpecRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.technicalSpecMapper = technicalSpecMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public TechnicalSpecResponse getByMotorcycleId(UUID motorcycleId) {
        TechnicalSpec spec = technicalSpecRepository.findByMotorcycleId(motorcycleId)
                .orElseThrow(() -> new ResourceNotFoundException("Technical spec not found for motorcycle ID: " + motorcycleId));
        return technicalSpecMapper.toResponse(spec);
    }

    @Override
    public TechnicalSpecResponse createOrUpdate(UUID motorcycleId, TechnicalSpecRequest request) {
        Motorcycle motorcycle = motorcycleRepository.findById(motorcycleId)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + motorcycleId));

        TechnicalSpec spec = technicalSpecRepository.findByMotorcycleId(motorcycleId)
                .map(existingSpec -> {
                    technicalSpecMapper.updateEntityFromRequest(request, existingSpec);
                    return existingSpec;
                })
                .orElseGet(() -> {
                    TechnicalSpec newSpec = technicalSpecMapper.toEntity(request, motorcycle);
                    motorcycle.setTechnicalSpec(newSpec);
                    return newSpec;
                });

        TechnicalSpec saved = technicalSpecRepository.save(spec);
        return technicalSpecMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID motorcycleId) {
        TechnicalSpec spec = technicalSpecRepository.findByMotorcycleId(motorcycleId)
                .orElseThrow(() -> new ResourceNotFoundException("Technical spec not found for motorcycle ID: " + motorcycleId));
        technicalSpecRepository.delete(spec);
    }
}
