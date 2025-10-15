package com.piyu.app;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/piyu/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createProduct(@RequestBody ProductDto productDto){
        productService.createProduct(productDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateProduct(@RequestBody ProductDto productDto){
        productService.updateProduct(productDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id){
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/get")
    public Page<Product> getProducts(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.getProducts(type, search, sort, page, size);
    }
}
