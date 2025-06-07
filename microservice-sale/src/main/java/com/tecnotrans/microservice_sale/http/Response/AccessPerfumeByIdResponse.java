package com.tecnotrans.microservice_sale.http.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessPerfumeByIdResponse {
    private String perfumeName;
    private int stock;
}
