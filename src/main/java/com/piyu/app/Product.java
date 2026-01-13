package com.piyu.app;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;
    private String size;
    private String model;
    private String type;
    private Long quantity;
    private String serialNumber;
    private Long netLandingPrice;
    private Date createdDate;
    private Date updatedDate;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date();
    }
}
