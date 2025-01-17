package com.soigo.romashkako.repository.specification;

import com.soigo.romashkako.model.Sale;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaleSpecification {
    public static Specification<Sale> hasNameAndProductId(
            String name,
            Long productId,
            BigDecimal minCost,
            BigDecimal maxCost
    ) {
        return (Root<Sale> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (productId != null) {
                predicates.add(criteriaBuilder.equal(root.get("product").get("id"), productId));
            }
            if (minCost != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minCost));
            }
            if (maxCost != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), maxCost));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
