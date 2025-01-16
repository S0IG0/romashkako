package com.soigo.romashkako.repository.specification;


import com.soigo.romashkako.model.Availability;
import com.soigo.romashkako.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> hasNameAndPriceAndAvailability(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Availability availability
    ) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (availability != null) {
                predicates.add(criteriaBuilder.equal(root.get("availability"), availability));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
