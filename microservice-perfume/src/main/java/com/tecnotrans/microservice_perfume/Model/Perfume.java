package com.tecnotrans.microservice_perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity//Le indico con el decorador que este sera el id de mi tabla
@Table(name = "PERFUME")//Con esto le digo que sera autoincrementable
@Data
@NoArgsConstructor
@AllArgsConstructor
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
