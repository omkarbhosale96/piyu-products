package com.piyu.app;

import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public interface ProductService {
    void createProduct(ProductDto productDto);

    void updateProduct(ProductDto productDto);

    void deleteProduct(Long id);

    Product getProduct(Long id);

    Page<Product> getProducts(String type, String search, int page, int size);

    ByteArrayInputStream exportProductsToExcel() throws IOException;
}
