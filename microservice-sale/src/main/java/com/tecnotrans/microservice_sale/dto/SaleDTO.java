package com.tecnotrans.microservice_sale.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long id;
    private Date date;
    private int qty;
    private Long idPerfume;
    private Long idUser;

}
