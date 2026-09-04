package com.vicentedev.api_re.repository.specification;

import com.vicentedev.api_re.entity.Dealership;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class DealershipSpecification {

    private DealershipSpecification() {
    }

    public static Specification<Dealership> withFilters(String state, String city, String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (state != null && !state.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("state")),
                        state.trim().toLowerCase()
                ));
            }

            if (city != null && !city.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("city")),
                        city.trim().toLowerCase()
                ));
            }

            if (query != null && !query.isBlank()) {
                String searchPattern = "%" + query.trim().toLowerCase() + "%";
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate addressLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), searchPattern);
                predicates.add(criteriaBuilder.or(nameLike, addressLike));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
