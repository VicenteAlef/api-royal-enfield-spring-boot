package com.vicentedev.api_re.repository;

import com.vicentedev.api_re.entity.TechnicalSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechnicalSpecRepository extends JpaRepository<TechnicalSpec, UUID> {

    Optional<TechnicalSpec> findByMotorcycleId(UUID motorcycleId);
}
