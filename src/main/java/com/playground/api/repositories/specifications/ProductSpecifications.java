package com.playground.api.repositories.specifications;

import com.playground.api.enums.ProductCategory;
import com.playground.api.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> inCategory(ProductCategory category) {
        return (root, query, cb) ->
                cb.equal(root.get("category"), category);
    }

    public static Specification<Product> hasStock(boolean hasStock) {
        return (root, query, cb) -> hasStock
                ? cb.greaterThan(root.get("stockQuantity"), 0)
                : cb.equal(root.get("stockQuantity"), 0);
    }

    public static Specification<Product> priceAtLeast(Double min) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static Specification<Product> priceAtMost(Double max) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("price"), max);
    }
}
