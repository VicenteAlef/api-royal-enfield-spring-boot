package com.vicentedev.api_re.repository.specification;

import com.vicentedev.api_re.entity.Role;
import com.vicentedev.api_re.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> withFilters(Boolean neverAccessed, Role role, String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (neverAccessed != null) {
                if (neverAccessed) {
                    predicates.add(criteriaBuilder.isNull(root.get("lastLoginAt")));
                } else {
                    predicates.add(criteriaBuilder.isNotNull(root.get("lastLoginAt")));
                }
            }

            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }

            if (query != null && !query.isBlank()) {
                String searchPattern = "%" + query.trim().toLowerCase() + "%";
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate emailLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern);
                predicates.add(criteriaBuilder.or(nameLike, emailLike));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
