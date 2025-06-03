package com.tecnotrans.microservice_perfume.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeDTO {

    private long id;
    private String name;
    private int stock;
    private float price;
    private String brand;
}
