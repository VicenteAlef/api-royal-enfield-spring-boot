package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.dto.request.DealershipCreateRequest;
import com.vicentedev.api_re.dto.request.DealershipUpdateRequest;
import com.vicentedev.api_re.dto.response.DealershipResponse;
import com.vicentedev.api_re.entity.Dealership;
import com.vicentedev.api_re.exception.ResourceNotFoundException;
import com.vicentedev.api_re.mapper.DealershipMapper;
import com.vicentedev.api_re.repository.DealershipRepository;
import com.vicentedev.api_re.repository.specification.DealershipSpecification;
import com.vicentedev.api_re.service.DealershipService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DealershipServiceImpl implements DealershipService {

    private final DealershipRepository dealershipRepository;
    private final DealershipMapper dealershipMapper;

    public DealershipServiceImpl(
            DealershipRepository dealershipRepository,
            DealershipMapper dealershipMapper
    ) {
        this.dealershipRepository = dealershipRepository;
        this.dealershipMapper = dealershipMapper;
    }

    @Override
    public DealershipResponse create(DealershipCreateRequest request) {
        Dealership dealership = dealershipMapper.toEntity(request);
        Dealership saved = dealershipRepository.save(dealership);
        return dealershipMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealershipResponse> list(String state, String city, String query, Pageable pageable) {
        Specification<Dealership> spec = DealershipSpecification.withFilters(state, city, query);
        Page<Dealership> dealerships = dealershipRepository.findAll(spec, pageable);
        return dealerships.map(dealershipMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DealershipResponse getById(UUID id) {
        Dealership dealership = dealershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dealership not found with ID: " + id));
        return dealershipMapper.toResponse(dealership);
    }

    @Override
    public DealershipResponse update(UUID id, DealershipUpdateRequest request) {
        Dealership dealership = dealershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dealership not found with ID: " + id));

        dealershipMapper.updateEntityFromRequest(request, dealership);
        Dealership saved = dealershipRepository.save(dealership);
        return dealershipMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        Dealership dealership = dealershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dealership not found with ID: " + id));

        dealershipRepository.delete(dealership);
    }
}
