package com.tecnotrans.microservice_sale.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.tecnotrans.microservice_sale.dto.PerfumeDTO;


@FeignClient(name = "msvc-perfume", url = "localhost:8090")
public interface PerfumeClient {

    @GetMapping("/api/v1/perfumes/access-perfume-by-id/{id}")
    PerfumeDTO accessPerfumeById(@PathVariable Long id);

    @GetMapping("/api/v1/perfumes/dameunnumero/{id}")
    int dameUnNumero(@PathVariable int id);

    @GetMapping("api/v1/perfumes/darPefume/{id}")
    PerfumeDTO dameUnPerfume(@PathVariable Long id);

    @PutMapping("")
    void updateStockDueToSale(@PathVariable Long id, int stockToRemove);
}
