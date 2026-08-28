package com.vicentedev.api_re.controller;

import com.vicentedev.api_re.dto.request.MotorcycleGalleryRequest;
import com.vicentedev.api_re.dto.response.MotorcycleGalleryResponse;
import com.vicentedev.api_re.service.MotorcycleGalleryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MotorcycleGalleryController {

    private final MotorcycleGalleryService galleryService;

    public MotorcycleGalleryController(MotorcycleGalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @PostMapping(value = "/motorcycles/{motorcycleId}/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MotorcycleGalleryResponse> addImageFile(
            @PathVariable UUID motorcycleId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "displayOrder", required = false, defaultValue = "0") Integer displayOrder
    ) {
        MotorcycleGalleryRequest request = new MotorcycleGalleryRequest(null, caption, displayOrder);
        MotorcycleGalleryResponse response = galleryService.addImage(motorcycleId, request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/motorcycles/{motorcycleId}/gallery/url", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MotorcycleGalleryResponse> addImageUrl(
            @PathVariable UUID motorcycleId,
            @Valid @RequestBody MotorcycleGalleryRequest request
    ) {
        MotorcycleGalleryResponse response = galleryService.addImage(motorcycleId, request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/motorcycles/{motorcycleId}/gallery")
    public ResponseEntity<List<MotorcycleGalleryResponse>> listGallery(@PathVariable UUID motorcycleId) {
        List<MotorcycleGalleryResponse> response = galleryService.getByMotorcycleId(motorcycleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/gallery/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MotorcycleGalleryResponse> updateImage(
            @PathVariable UUID id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder
    ) {
        MotorcycleGalleryRequest request = new MotorcycleGalleryRequest(null, caption, displayOrder);
        MotorcycleGalleryResponse response = galleryService.updateImage(id, request, file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/gallery/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        galleryService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
