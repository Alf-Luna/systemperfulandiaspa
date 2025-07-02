package com.tecnotrans.microservice_sale.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a sale")
public class SaleDTO {

    @Schema(description = "Identifier of the sale", example = "100")
    private Long id;

    @Schema(description = "Date and time when the sale occurred", example = "2025-06-30T15:45:00")
    private LocalDateTime date;

    @Schema(description = "Quantity of perfumes sold", example = "2")
    private int qty;

    @Schema(description = "ID of the perfume being sold", example = "5")
    private Long idPerfume;

    @Schema(description = "ID of the user who made the purchase", example = "42")
    private Long idUser;

}
