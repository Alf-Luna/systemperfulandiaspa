package com.tecnotrans.microservice_sale.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a perfume")
public class PerfumeDTO {

    @Schema(description = "Identifier of the perfume", example = "1")
    private Long id;

    @Schema(description = "Name of the perfume", example = "Eau de Parfum")
    private String name;

    @Schema(description = "Available stock of the perfume", example = "100")
    private int stock;

    @Schema(description = "Price of the perfume", example = "59.99")
    private float price;

    @Schema(description = "Brand of the perfume", example = "Chanel")
    private String brand;
}
