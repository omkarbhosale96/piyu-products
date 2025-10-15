package com.piyu.app;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ProductDto {

    private Long id;
    private String company;
    private String model;
    private String type;
    private Long quantity;
    private Double price;
    private Double netLandingPrice;
}
