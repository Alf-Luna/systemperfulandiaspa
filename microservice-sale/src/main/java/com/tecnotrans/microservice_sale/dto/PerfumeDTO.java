package com.tecnotrans.microservice_sale.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeDTO {

    private Long id;
    private String name;
    private int stock;
    private float price;
    private String brand;
}
