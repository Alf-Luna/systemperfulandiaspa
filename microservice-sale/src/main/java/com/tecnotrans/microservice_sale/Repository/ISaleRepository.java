package com.tecnotrans.microservice_sale.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecnotrans.microservice_sale.Model.Sale;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {

}
