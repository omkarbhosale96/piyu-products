package com.piyu.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter@Setter
@ToString
public class ProductDto {

    private Long id;
    private String company;
    private String size;
    private String model;
    private String type;
    private Long quantity;
    private String serialNumber;
    private Long netLandingPrice;
}
