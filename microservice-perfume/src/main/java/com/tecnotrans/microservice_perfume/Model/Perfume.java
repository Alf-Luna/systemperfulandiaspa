package com.tecnotrans.microservice_perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "PERFUME")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a perfume entity stored in the database.")
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifier of the perfume", example = "1")
    private Long id;

    @Column(name = "NAME", length = 40, nullable = false)
    @Schema(description = "Name of the perfume", example = "Eau de Parfum")
    private String name;
    
    @Column(name = "STOCK")
    @Schema(description = "Available stock of the perfume", example = "100")
    private int stock;
    
    @Column(name = "PRICE")
    @Schema(description = "Price of the perfume", example = "59.99")
    private float price;
    
    @Column(name = "BRAND", length = 40, nullable = false)
    @Schema(description = "Brand of the perfume", example = "Chanel")
    private String brand;
}
