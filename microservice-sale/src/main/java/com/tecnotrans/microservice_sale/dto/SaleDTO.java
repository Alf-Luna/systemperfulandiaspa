package com.tecnotrans.microservice_sale.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long id;
    private LocalDateTime date;
    private int qty;
    private Long idPerfume;
    private Long idUser;

}
