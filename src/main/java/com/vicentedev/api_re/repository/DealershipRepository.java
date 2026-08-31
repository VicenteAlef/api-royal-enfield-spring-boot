package com.vicentedev.api_re.repository;

import com.vicentedev.api_re.entity.Dealership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DealershipRepository extends JpaRepository<Dealership, UUID>, JpaSpecificationExecutor<Dealership> {

    Page<Dealership> findByStateIgnoreCase(String state, Pageable pageable);

    Page<Dealership> findByCityIgnoreCaseAndStateIgnoreCase(String city, String state, Pageable pageable);
}
