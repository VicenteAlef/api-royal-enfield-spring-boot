package com.vicentedev.api_re.repository;

import com.vicentedev.api_re.entity.MotorcycleGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MotorcycleGalleryRepository extends JpaRepository<MotorcycleGallery, UUID> {

    List<MotorcycleGallery> findByMotorcycleIdOrderByDisplayOrderAsc(UUID motorcycleId);
}
