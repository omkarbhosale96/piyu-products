package com.piyu.app;

import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> hasType(String type) {
        return (root, query, cb) ->
                (type == null || type.isEmpty()) ? null :
                        cb.equal(root.get("type"), type);
    }

    public static Specification<Product> searchByCompanyOrModel(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) return null;
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("company")), pattern),
                    cb.like(cb.lower(root.get("model")), pattern)
            );
        };
    }
}

