package com.tecnotrans.microservice_sale.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tecnotrans.microservice_sale.dto.PerfumeDTO;


@FeignClient(name = "msvc-perfume", url = "localhost:8090")
public interface PerfumeClient {

    @GetMapping("/api/v1/perfume/access-perfume-by-id/{id}")
    PerfumeDTO accessPerfumeById(@PathVariable Long id);
}
