package com.vicentedev.api_re.repository;

import com.vicentedev.api_re.entity.MotorcycleVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MotorcycleVariantRepository extends JpaRepository<MotorcycleVariant, UUID> {

    List<MotorcycleVariant> findByMotorcycleId(UUID motorcycleId);

    List<MotorcycleVariant> findByMotorcycleIdAndActiveTrue(UUID motorcycleId);
}
