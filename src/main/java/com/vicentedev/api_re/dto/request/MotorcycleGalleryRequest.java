package com.vicentedev.api_re.dto.request;

import jakarta.validation.constraints.Size;

public record MotorcycleGalleryRequest(
        @Size(max = 255, message = "Image URL cannot exceed 255 characters")
        String imageUrl,

        @Size(max = 150, message = "Caption cannot exceed 150 characters")
        String caption,

        Integer displayOrder
) {
}
