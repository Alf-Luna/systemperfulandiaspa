package com.tecnotrans.microservice_sale.Service;

import com.tecnotrans.microservice_sale.Model.Sale;
///import com.tecnotrans.microservice_sale.http.response.StudentByCourseResponse; /// no tenemos un equivalente a esto
import com.tecnotrans.microservice_sale.http.Response.AccessPerfumeByIdResponse;

import java.util.List;


public interface ISaleService {
    
    List<Sale> findAll();

    Sale findById(Long id);

    Sale save(Sale course);

    AccessPerfumeByIdResponse accessPerfumeById(Long idPerfume);

    void deleteById(Long id);

}
