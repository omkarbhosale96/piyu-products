package com.piyu.app;

import org.springframework.data.domain.Page;


public interface ProductService {
    void createProduct(ProductDto productDto);

    void updateProduct(ProductDto productDto);

    void deleteProduct(Long id);

    Product getProduct(Long id);

    Page<Product> getProducts(String type, String search, String sort, int page, int size);
}
