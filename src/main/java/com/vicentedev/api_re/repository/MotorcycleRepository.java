package com.vicentedev.api_re.repository;

import com.vicentedev.api_re.entity.Motorcycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID>, JpaSpecificationExecutor<Motorcycle> {

    Page<Motorcycle> findByActiveTrue(Pageable pageable);

    Page<Motorcycle> findByFamilyIgnoreCase(String family, Pageable pageable);

    Page<Motorcycle> findByFamilyIgnoreCaseAndActiveTrue(String family, Pageable pageable);

    Optional<Motorcycle> findByIdAndActiveTrue(UUID id);
}
