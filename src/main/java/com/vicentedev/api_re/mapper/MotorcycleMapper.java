package com.vicentedev.api_re.mapper;

import com.vicentedev.api_re.dto.request.MotorcycleCreateRequest;
import com.vicentedev.api_re.dto.request.MotorcycleUpdateRequest;
import com.vicentedev.api_re.dto.response.MotorcycleDetailResponse;
import com.vicentedev.api_re.dto.response.MotorcycleSummaryResponse;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.MotorcycleGallery;
import com.vicentedev.api_re.entity.MotorcycleVariant;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class MotorcycleMapper {

    private final TechnicalSpecMapper technicalSpecMapper;
    private final VariantMapper variantMapper;
    private final GalleryMapper galleryMapper;

    public MotorcycleMapper(
            TechnicalSpecMapper technicalSpecMapper,
            VariantMapper variantMapper,
            GalleryMapper galleryMapper
    ) {
        this.technicalSpecMapper = technicalSpecMapper;
        this.variantMapper = variantMapper;
        this.galleryMapper = galleryMapper;
    }

    public Motorcycle toEntity(MotorcycleCreateRequest request) {
        if (request == null) {
            return null;
        }

        Motorcycle motorcycle = Motorcycle.builder()
                .modelName(request.modelName())
                .family(request.family())
                .engineCc(request.engineCc())
                .startingPrice(request.startingPrice())
                .description(request.description())
                .active(request.active() != null ? request.active() : true)
                .build();

        if (request.technicalSpec() != null) {
            motorcycle.setTechnicalSpec(technicalSpecMapper.toEntity(request.technicalSpec(), motorcycle));
        }

        return motorcycle;
    }

    public void updateEntityFromRequest(MotorcycleUpdateRequest request, Motorcycle motorcycle) {
        if (request == null || motorcycle == null) {
            return;
        }

        motorcycle.setModelName(request.modelName());
        motorcycle.setFamily(request.family());
        motorcycle.setEngineCc(request.engineCc());
        motorcycle.setStartingPrice(request.startingPrice());
        motorcycle.setDescription(request.description());
        if (request.active() != null) {
            motorcycle.setActive(request.active());
        }
    }

    public MotorcycleSummaryResponse toSummaryResponse(Motorcycle entity) {
        if (entity == null) {
            return null;
        }

        String mainImageUrl = null;
        if (entity.getVariants() != null && !entity.getVariants().isEmpty()) {
            mainImageUrl = entity.getVariants().stream()
                    .filter(v -> v.getImageUrl() != null && !v.getImageUrl().isBlank())
                    .map(MotorcycleVariant::getImageUrl)
                    .findFirst()
                    .orElse(null);
        }

        if (mainImageUrl == null && entity.getGallery() != null && !entity.getGallery().isEmpty()) {
            mainImageUrl = entity.getGallery().stream()
                    .min(Comparator.comparingInt(MotorcycleGallery::getDisplayOrder))
                    .map(MotorcycleGallery::getImageUrl)
                    .orElse(null);
        }

        int variantCount = entity.getVariants() != null ? entity.getVariants().size() : 0;

        return new MotorcycleSummaryResponse(
                entity.getId(),
                entity.getModelName(),
                entity.getFamily(),
                entity.getEngineCc(),
                entity.getStartingPrice(),
                entity.getDescription(),
                entity.getActive(),
                mainImageUrl,
                variantCount,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public MotorcycleDetailResponse toDetailResponse(Motorcycle entity) {
        if (entity == null) {
            return null;
        }

        return new MotorcycleDetailResponse(
                entity.getId(),
                entity.getModelName(),
                entity.getFamily(),
                entity.getEngineCc(),
                entity.getStartingPrice(),
                entity.getDescription(),
                entity.getActive(),
                technicalSpecMapper.toResponse(entity.getTechnicalSpec()),
                variantMapper.toResponseList(entity.getVariants()),
                galleryMapper.toResponseList(entity.getGallery()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public List<MotorcycleSummaryResponse> toSummaryResponseList(List<Motorcycle> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toSummaryResponse).toList();
    }
}
