package com.piyu.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
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

    private static final String[] HEADERS = {
            "ID", "Company", "Size", "Model", "Type", "Quantity", "Net Landing Price", "Created Date", "Updated Date"
    };

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void createProduct(ProductDto productDto) {
        System.out.println(productDto);
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
            if(Objects.nonNull(productDto.getSize())){
                product.setSize(productDto.getSize());
            }
            if(Objects.nonNull(productDto.getSerialNumber())){
                product.setSerialNumber(productDto.getSerialNumber());
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
    public Page<Product> getProducts(String type, String search, int page, int size) {
        Specification<Product> spec = Specification
                .where(ProductSpecifications.hasType(type))
                .and(ProductSpecifications.searchByCompanyOrModel(search));

        Sort sortBy = Sort.by("createdDate").descending();

        Pageable pageable = PageRequest.of(page, size, sortBy);

        return productRepository.findAll(spec, pageable);
    }

    @Override
    public ByteArrayInputStream exportProductsToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            List<Product> products = productRepository.findAll();
            Sheet sheet = workbook.createSheet("Products");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(product.getId() != null ? product.getId() : 0);
                row.createCell(1).setCellValue(product.getCompany() != null ? product.getCompany() : "");
                row.createCell(2).setCellValue(product.getSize() != null ? product.getSize() : "");
                row.createCell(3).setCellValue(product.getModel() != null ? product.getModel() : "");
                row.createCell(4).setCellValue(product.getType() != null ? product.getType() : "");
                row.createCell(5).setCellValue(product.getQuantity() != null ? product.getQuantity() : 0);
                row.createCell(6).setCellValue(product.getNetLandingPrice() != null ? product.getNetLandingPrice() : 0);
                row.createCell(7).setCellValue(product.getCreatedDate() != null ? DATE_FORMAT.format(product.getCreatedDate()) : "");
                row.createCell(8).setCellValue(product.getUpdatedDate() != null ? DATE_FORMAT.format(product.getUpdatedDate()) : "");
            }

            // Auto-size columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

}
