package com.tecnotrans.microservice_sale.Service;

import com.tecnotrans.microservice_sale.Model.Sale;
///import com.tecnotrans.microservice_sale.http.response.StudentByCourseResponse; /// no tenemos un equivalente a esto

import java.util.List;


public interface ISaleService {
    
    List<Sale> findAll();

    Sale findById(Long id);

    void save(Sale course);

    ///StudentByCourseResponse findStudentsByIdCourse(Long idCourse); no tenemos un equivalente a esto

    void deleteById(Long id);

}
