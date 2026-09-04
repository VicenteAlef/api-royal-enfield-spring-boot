package com.vicentedev.api_re.mapper;

import com.vicentedev.api_re.dto.request.TestRideCreateRequest;
import com.vicentedev.api_re.dto.response.DealershipResponse;
import com.vicentedev.api_re.dto.response.TestRideResponse;
import com.vicentedev.api_re.entity.Dealership;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.MotorcycleVariant;
import com.vicentedev.api_re.entity.TestRide;
import com.vicentedev.api_re.entity.TestRideStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestRideMapper {

    private final DealershipMapper dealershipMapper;

    public TestRideMapper(DealershipMapper dealershipMapper) {
        this.dealershipMapper = dealershipMapper;
    }

    public TestRide toEntity(
            TestRideCreateRequest request,
            Motorcycle motorcycle,
            MotorcycleVariant variant,
            Dealership dealership
    ) {
        if (request == null) {
            return null;
        }

        return TestRide.builder()
                .customerName(request.customerName().trim())
                .customerEmail(request.customerEmail().trim().toLowerCase())
                .customerPhone(request.customerPhone().trim())
                .preferredDate(request.preferredDate())
                .status(TestRideStatus.PENDING)
                .motorcycle(motorcycle)
                .variant(variant)
                .dealership(dealership)
                .build();
    }

    public TestRideResponse toResponse(TestRide entity) {
        if (entity == null) {
            return null;
        }

        TestRideResponse.MotorcycleSummary motorcycleSummary = null;
        if (entity.getMotorcycle() != null) {
            motorcycleSummary = new TestRideResponse.MotorcycleSummary(
                    entity.getMotorcycle().getId(),
                    entity.getMotorcycle().getModelName(),
                    entity.getMotorcycle().getFamily(),
                    entity.getMotorcycle().getEngineCc()
            );
        }

        TestRideResponse.VariantSummary variantSummary = null;
        if (entity.getVariant() != null) {
            variantSummary = new TestRideResponse.VariantSummary(
                    entity.getVariant().getId(),
                    entity.getVariant().getVariantName(),
                    entity.getVariant().getColorName(),
                    entity.getVariant().getImageUrl()
            );
        }

        DealershipResponse dealershipResponse = dealershipMapper.toResponse(entity.getDealership());

        return new TestRideResponse(
                entity.getId(),
                entity.getCustomerName(),
                entity.getCustomerEmail(),
                entity.getCustomerPhone(),
                entity.getPreferredDate(),
                entity.getStatus(),
                motorcycleSummary,
                variantSummary,
                dealershipResponse,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public List<TestRideResponse> toResponseList(List<TestRide> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(this::toResponse).toList();
    }
}
