package com.alex.eshop.filterSpecifications;

import com.alex.eshop.entity.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {
    public static Specification<Item> hasNameContaining(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Item> hasImage(Boolean hasImage) {
        return (root, query, criteriaBuilder) -> {
            if (Boolean.TRUE.equals(hasImage)) {
                return criteriaBuilder.isNotNull(root.get("imageSrc"));
            } else if (Boolean.FALSE.equals(hasImage)) {
                return criteriaBuilder.isNull(root.get("imageSrc"));
            }
            return null;
        };
    }

    public static Specification<Item> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId != null) {
                return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
            }
            return null;
        };
    }
}