package com.vicentedev.api_re.mapper;

import com.vicentedev.api_re.dto.request.DealershipCreateRequest;
import com.vicentedev.api_re.dto.request.DealershipUpdateRequest;
import com.vicentedev.api_re.dto.response.DealershipResponse;
import com.vicentedev.api_re.entity.Dealership;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DealershipMapper {

    public Dealership toEntity(DealershipCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Dealership.builder()
                .name(request.name().trim())
                .city(request.city().trim())
                .state(request.state().trim().toUpperCase())
                .address(request.address().trim())
                .phone(request.phone() != null ? request.phone().trim() : null)
                .email(request.email() != null ? request.email().trim().toLowerCase() : null)
                .build();
    }

    public void updateEntityFromRequest(DealershipUpdateRequest request, Dealership entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setName(request.name().trim());
        entity.setCity(request.city().trim());
        entity.setState(request.state().trim().toUpperCase());
        entity.setAddress(request.address().trim());
        entity.setPhone(request.phone() != null ? request.phone().trim() : null);
        entity.setEmail(request.email() != null ? request.email().trim().toLowerCase() : null);
    }

    public DealershipResponse toResponse(Dealership entity) {
        if (entity == null) {
            return null;
        }

        return new DealershipResponse(
                entity.getId(),
                entity.getName(),
                entity.getCity(),
                entity.getState(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public List<DealershipResponse> toResponseList(List<Dealership> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(this::toResponse).toList();
    }
}
