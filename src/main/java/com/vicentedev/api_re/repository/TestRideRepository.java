package com.vicentedev.api_re.repository;

import com.vicentedev.api_re.entity.TestRide;
import com.vicentedev.api_re.entity.TestRideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestRideRepository extends JpaRepository<TestRide, UUID>, JpaSpecificationExecutor<TestRide> {

    Page<TestRide> findByDealershipId(UUID dealershipId, Pageable pageable);

    Page<TestRide> findByCustomerEmailIgnoreCase(String customerEmail, Pageable pageable);

    Page<TestRide> findByStatus(TestRideStatus status, Pageable pageable);
}
