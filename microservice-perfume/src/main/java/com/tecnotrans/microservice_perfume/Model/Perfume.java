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

@Entity
@Table(name = "PERFUME")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 40, nullable = false)
    private String name;
    
    @Column(name = "STOCK")
    private int stock;
    
    @Column(name = "PRICE")
    private float price;
    
    @Column(name = "BRAND", length = 40, nullable = false)
    private String brand;
}
