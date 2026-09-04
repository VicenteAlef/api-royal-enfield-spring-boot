package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.dto.request.TestRideCreateRequest;
import com.vicentedev.api_re.dto.request.TestRideStatusUpdateRequest;
import com.vicentedev.api_re.dto.response.TestRideResponse;
import com.vicentedev.api_re.entity.Dealership;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.MotorcycleVariant;
import com.vicentedev.api_re.entity.TestRide;
import com.vicentedev.api_re.entity.TestRideStatus;
import com.vicentedev.api_re.exception.BusinessException;
import com.vicentedev.api_re.exception.ResourceNotFoundException;
import com.vicentedev.api_re.mapper.TestRideMapper;
import com.vicentedev.api_re.repository.DealershipRepository;
import com.vicentedev.api_re.repository.MotorcycleRepository;
import com.vicentedev.api_re.repository.MotorcycleVariantRepository;
import com.vicentedev.api_re.repository.TestRideRepository;
import com.vicentedev.api_re.repository.specification.TestRideSpecification;
import com.vicentedev.api_re.service.TestRideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
public class TestRideServiceImpl implements TestRideService {

    private final TestRideRepository testRideRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final MotorcycleVariantRepository variantRepository;
    private final DealershipRepository dealershipRepository;
    private final TestRideMapper testRideMapper;

    public TestRideServiceImpl(
            TestRideRepository testRideRepository,
            MotorcycleRepository motorcycleRepository,
            MotorcycleVariantRepository variantRepository,
            DealershipRepository dealershipRepository,
            TestRideMapper testRideMapper
    ) {
        this.testRideRepository = testRideRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.variantRepository = variantRepository;
        this.dealershipRepository = dealershipRepository;
        this.testRideMapper = testRideMapper;
    }

    @Override
    public TestRideResponse create(TestRideCreateRequest request) {
        Motorcycle motorcycle = motorcycleRepository.findById(request.motorcycleId())
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + request.motorcycleId()));

        if (Boolean.FALSE.equals(motorcycle.getActive())) {
            throw new BusinessException("Cannot schedule test ride for an inactive motorcycle");
        }

        Dealership dealership = dealershipRepository.findById(request.dealershipId())
                .orElseThrow(() -> new ResourceNotFoundException("Dealership not found with ID: " + request.dealershipId()));

        MotorcycleVariant variant = null;
        if (request.variantId() != null) {
            variant = variantRepository.findById(request.variantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Motorcycle variant not found with ID: " + request.variantId()));

            if (!variant.getMotorcycle().getId().equals(motorcycle.getId())) {
                throw new BusinessException("The selected variant does not belong to motorcycle: " + motorcycle.getModelName());
            }
        }

        TestRide testRide = testRideMapper.toEntity(request, motorcycle, variant, dealership);
        TestRide saved = testRideRepository.save(testRide);
        return testRideMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestRideResponse> list(
            UUID dealershipId,
            UUID motorcycleId,
            TestRideStatus status,
            String customerEmail,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            Pageable pageable
    ) {
        Specification<TestRide> spec = TestRideSpecification.withFilters(
                dealershipId, motorcycleId, status, customerEmail, startDate, endDate
        );
        Page<TestRide> testRides = testRideRepository.findAll(spec, pageable);
        return testRides.map(testRideMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TestRideResponse getById(UUID id) {
        TestRide testRide = testRideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test ride not found with ID: " + id));
        return testRideMapper.toResponse(testRide);
    }

    @Override
    public TestRideResponse updateStatus(UUID id, TestRideStatusUpdateRequest request) {
        TestRide testRide = testRideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test ride not found with ID: " + id));

        testRide.setStatus(request.status());
        TestRide saved = testRideRepository.save(testRide);
        return testRideMapper.toResponse(saved);
    }

    @Override
    public TestRideResponse cancel(UUID id) {
        TestRide testRide = testRideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test ride not found with ID: " + id));

        if (testRide.getStatus() == TestRideStatus.COMPLETED) {
            throw new BusinessException("Cannot cancel a completed test ride");
        }

        testRide.setStatus(TestRideStatus.CANCELLED);
        TestRide saved = testRideRepository.save(testRide);
        return testRideMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        TestRide testRide = testRideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test ride not found with ID: " + id));

        testRideRepository.delete(testRide);
    }
}
