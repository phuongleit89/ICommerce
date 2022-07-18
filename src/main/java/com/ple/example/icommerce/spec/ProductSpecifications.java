package com.ple.example.icommerce.spec;

import com.ple.example.icommerce.entity.tenant.Product;
import com.ple.example.icommerce.entity.tenant.Product_;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<Product> nameLike(String name) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(name)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get(Product_.sku)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> skuLike(String sku) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(sku)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get(Product_.sku)), "%" + sku.toLowerCase() + "%");
        };
    }

    public static Specification<Product> priceInRange(Double min, Double max) {
        return (root, query, cb) -> {
            Predicate predicateMin = min == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get(Product_.price), min);
            Predicate predicateMax = max == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get(Product_.price), max);
            return cb.and(predicateMin, predicateMax);
        };
    }

    public static Specification<Product> quantityInRange(Integer min, Integer max) {
        return (root, query, cb) -> {
            Predicate predicateMin = min == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get(Product_.quantity), min);
            Predicate predicateMax = max == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get(Product_.quantity), max);
            return cb.and(predicateMin, predicateMax);
        };
    }

}
