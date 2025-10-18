package com.piyu.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductServiceImpl(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void createProduct(ProductDto productDto) {
        Product product = objectMapper.convertValue(productDto, Product.class);
        productRepository.save(product);
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            if(Objects.nonNull(productDto.getCompany())){
                product.setCompany(productDto.getCompany());
            }
            if(Objects.nonNull(productDto.getModel())){
                product.setModel(productDto.getModel());
            }
            if(Objects.nonNull(productDto.getType())){
                product.setType(productDto.getType());
            }
            if(Objects.nonNull(productDto.getPrice())){
                product.setPrice(productDto.getPrice());
            }
            if(Objects.nonNull(productDto.getNetLandingPrice())){
                product.setNetLandingPrice(productDto.getNetLandingPrice());
            }
            if(Objects.nonNull(productDto.getQuantity())){
                product.setQuantity(productDto.getQuantity());
            }
            productRepository.save(product);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        if (Objects.nonNull(id) && productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    @Override
    public Product getProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElseThrow();
    }

    @Override
    public Page<Product> getProducts(String type, String search, String sort, int page, int size) {
        Specification<Product> spec = Specification
                .where(ProductSpecifications.hasType(type))
                .and(ProductSpecifications.searchByCompanyOrModel(search));

        Sort sortBy = Sort.unsorted();
        if ("asc".equalsIgnoreCase(sort)) {
            sortBy = Sort.by("price").ascending();
        } else if ("desc".equalsIgnoreCase(sort)) {
            sortBy = Sort.by("price").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sortBy);

        return productRepository.findAll(spec, pageable);
    }

    @Scheduled(cron = "${cron.job.expression}")
    public void healthCheckSchedular(){
        log.info("App is running...");
    }
}
