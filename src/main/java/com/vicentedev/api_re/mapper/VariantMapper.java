package com.vicentedev.api_re.mapper;

import com.vicentedev.api_re.dto.request.MotorcycleVariantRequest;
import com.vicentedev.api_re.dto.response.MotorcycleVariantResponse;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.MotorcycleVariant;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class VariantMapper {

    public MotorcycleVariant toEntity(MotorcycleVariantRequest request, Motorcycle motorcycle) {
        if (request == null) {
            return null;
        }

        return MotorcycleVariant.builder()
                .motorcycle(motorcycle)
                .variantName(request.variantName())
                .colorName(request.colorName())
                .hexColorCode(request.hexColorCode())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .includedAccessories(request.includedAccessories())
                .active(request.active() != null ? request.active() : true)
                .build();
    }

    public void updateEntityFromRequest(MotorcycleVariantRequest request, MotorcycleVariant variant) {
        if (request == null || variant == null) {
            return;
        }

        variant.setVariantName(request.variantName());
        variant.setColorName(request.colorName());
        variant.setHexColorCode(request.hexColorCode());
        variant.setPrice(request.price());
        if (request.imageUrl() != null) {
            variant.setImageUrl(request.imageUrl());
        }
        variant.setIncludedAccessories(request.includedAccessories());
        if (request.active() != null) {
            variant.setActive(request.active());
        }
    }

    public MotorcycleVariantResponse toResponse(MotorcycleVariant entity) {
        if (entity == null) {
            return null;
        }

        return new MotorcycleVariantResponse(
                entity.getId(),
                entity.getMotorcycle() != null ? entity.getMotorcycle().getId() : null,
                entity.getVariantName(),
                entity.getColorName(),
                entity.getHexColorCode(),
                entity.getPrice(),
                entity.getImageUrl(),
                entity.getIncludedAccessories(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public List<MotorcycleVariantResponse> toResponseList(List<MotorcycleVariant> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toResponse).toList();
    }
}
