package com.tecnotrans.microservice_sale.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "msvc-user", url = "localhost:9080")
public interface UserClient {

    @GetMapping("api/v1/users/validateUser/{id}")
    boolean validateUser(@PathVariable Long id);

}
