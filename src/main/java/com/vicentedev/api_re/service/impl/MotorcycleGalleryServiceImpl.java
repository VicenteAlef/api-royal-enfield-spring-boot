package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.dto.request.MotorcycleGalleryRequest;
import com.vicentedev.api_re.dto.response.MotorcycleGalleryResponse;
import com.vicentedev.api_re.entity.Motorcycle;
import com.vicentedev.api_re.entity.MotorcycleGallery;
import com.vicentedev.api_re.exception.ResourceNotFoundException;
import com.vicentedev.api_re.mapper.GalleryMapper;
import com.vicentedev.api_re.repository.MotorcycleGalleryRepository;
import com.vicentedev.api_re.repository.MotorcycleRepository;
import com.vicentedev.api_re.service.FileStorageService;
import com.vicentedev.api_re.service.MotorcycleGalleryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MotorcycleGalleryServiceImpl implements MotorcycleGalleryService {

    private final MotorcycleGalleryRepository galleryRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final GalleryMapper galleryMapper;
    private final FileStorageService fileStorageService;

    public MotorcycleGalleryServiceImpl(
            MotorcycleGalleryRepository galleryRepository,
            MotorcycleRepository motorcycleRepository,
            GalleryMapper galleryMapper,
            FileStorageService fileStorageService
    ) {
        this.galleryRepository = galleryRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.galleryMapper = galleryMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public MotorcycleGalleryResponse addImage(UUID motorcycleId, MotorcycleGalleryRequest request, MultipartFile file) {
        Motorcycle motorcycle = motorcycleRepository.findById(motorcycleId)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + motorcycleId));

        String imageUrl;
        if (file != null && !file.isEmpty()) {
            imageUrl = fileStorageService.storeFile(file, "gallery");
        } else if (request != null && request.imageUrl() != null && !request.imageUrl().isBlank()) {
            imageUrl = request.imageUrl();
        } else {
            throw new IllegalArgumentException("Either file or imageUrl must be provided");
        }

        MotorcycleGallery item = galleryMapper.toEntity(request, motorcycle, imageUrl);
        MotorcycleGallery saved = galleryRepository.save(item);
        return galleryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MotorcycleGalleryResponse> getByMotorcycleId(UUID motorcycleId) {
        if (!motorcycleRepository.existsById(motorcycleId)) {
            throw new ResourceNotFoundException("Motorcycle not found with ID: " + motorcycleId);
        }
        List<MotorcycleGallery> items = galleryRepository.findByMotorcycleIdOrderByDisplayOrderAsc(motorcycleId);
        return galleryMapper.toResponseList(items);
    }

    @Override
    public MotorcycleGalleryResponse updateImage(UUID id, MotorcycleGalleryRequest request, MultipartFile file) {
        MotorcycleGallery item = galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery item not found with ID: " + id));

        if (file != null && !file.isEmpty()) {
            if (item.getImageUrl() != null && !item.getImageUrl().isBlank()) {
                fileStorageService.deleteFile(item.getImageUrl());
            }
            String imageUrl = fileStorageService.storeFile(file, "gallery");
            item.setImageUrl(imageUrl);
        } else if (request != null && request.imageUrl() != null && !request.imageUrl().isBlank()) {
            item.setImageUrl(request.imageUrl());
        }

        if (request != null) {
            if (request.caption() != null) {
                item.setCaption(request.caption());
            }
            if (request.displayOrder() != null) {
                item.setDisplayOrder(request.displayOrder());
            }
        }

        MotorcycleGallery saved = galleryRepository.save(item);
        return galleryMapper.toResponse(saved);
    }

    @Override
    public void deleteImage(UUID id) {
        MotorcycleGallery item = galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery item not found with ID: " + id));

        if (item.getImageUrl() != null && !item.getImageUrl().isBlank()) {
            fileStorageService.deleteFile(item.getImageUrl());
        }

        galleryRepository.delete(item);
    }
}
