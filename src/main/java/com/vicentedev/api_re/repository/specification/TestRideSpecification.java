package com.vicentedev.api_re.repository.specification;

import com.vicentedev.api_re.entity.TestRide;
import com.vicentedev.api_re.entity.TestRideStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TestRideSpecification {

    private TestRideSpecification() {
    }

    public static Specification<TestRide> withFilters(
            UUID dealershipId,
            UUID motorcycleId,
            TestRideStatus status,
            String customerEmail,
            OffsetDateTime startDate,
            OffsetDateTime endDate
    ) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dealershipId != null) {
                predicates.add(criteriaBuilder.equal(root.get("dealership").get("id"), dealershipId));
            }

            if (motorcycleId != null) {
                predicates.add(criteriaBuilder.equal(root.get("motorcycle").get("id"), motorcycleId));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (customerEmail != null && !customerEmail.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("customerEmail")),
                        customerEmail.trim().toLowerCase()
                ));
            }

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("preferredDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("preferredDate"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
