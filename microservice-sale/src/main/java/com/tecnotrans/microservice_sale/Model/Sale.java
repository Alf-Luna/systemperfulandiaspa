package com.tecnotrans.microservice_sale.Model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Represents a Sale stored in the system")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifier of the sale", example = "100")
    private Long id;

    @Column(name = "DATE")
    @Schema(description = "Date and time when the sale occurred", example = "2025-06-30T15:45:00")
    private LocalDateTime date;

    @Column(name = "QUANTITY", nullable = false)
    @Schema(description = "Quantity of perfumes sold", example = "2")
    private int qty;
    
    @Column(name = "ID_PERFUME", nullable = false)
    @Schema(description = "ID of the perfume being sold", example = "5")
    private Long idPerfume;

    @Column(name = "ID_USER", nullable = false)
    @Schema(description = "ID of the user who made the purchase", example = "42")
    private Long idUser;
}
