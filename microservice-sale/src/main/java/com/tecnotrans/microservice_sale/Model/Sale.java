package com.tecnotrans.microservice_sale.Model;

import java.time.LocalDateTime;

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

@Data
@Entity
@Builder
@Table(name="SALE")
@AllArgsConstructor
@NoArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATE")
    private LocalDateTime date;

    @Column(name = "QUANTITY", nullable = false)
    private int qty;
    
    @Column(name = "ID_PERFUME", nullable = false)
    private Long idPerfume;

    @Column(name = "ID_USER", nullable = false)
    private Long idUser;
}
