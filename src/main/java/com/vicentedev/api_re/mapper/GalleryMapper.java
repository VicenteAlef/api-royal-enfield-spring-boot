package com.vicentedev.api_re.mapper;

import com.vicentedev.api_re.dto.request.MotorcycleGalleryRequest;
import com.vicentedev.api_re.dto.response.MotorcycleGalleryResponse;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.MotorcycleGallery;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GalleryMapper {

    public MotorcycleGallery toEntity(MotorcycleGalleryRequest request, Motorcycle motorcycle, String imageUrl) {
        return MotorcycleGallery.builder()
                .motorcycle(motorcycle)
                .imageUrl(imageUrl != null ? imageUrl : request.imageUrl())
                .caption(request != null ? request.caption() : null)
                .displayOrder(request != null && request.displayOrder() != null ? request.displayOrder() : 0)
                .build();
    }

    public MotorcycleGalleryResponse toResponse(MotorcycleGallery entity) {
        if (entity == null) {
            return null;
        }

        return new MotorcycleGalleryResponse(
                entity.getId(),
                entity.getMotorcycle() != null ? entity.getMotorcycle().getId() : null,
                entity.getImageUrl(),
                entity.getCaption(),
                entity.getDisplayOrder(),
                entity.getCreatedAt()
        );
    }

    public List<MotorcycleGalleryResponse> toResponseList(List<MotorcycleGallery> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toResponse).toList();
    }
}
