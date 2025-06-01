package com.tecnotrans.microservice_perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NAME")
    private String name;
    
    @Column(name = "STOCK")
    private int stock;
    
    @Column(name = "PRICE")
    private float price;
    
    @Column(name = "BRAND")
    private String brand;
}
