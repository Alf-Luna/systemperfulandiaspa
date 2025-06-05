package com.tecnotrans.microservice_perfume.Service;

import java.util.List;

import com.tecnotrans.microservice_perfume.Model.Perfume;

public interface IPerfumeService {
    List<Perfume> getPerfumes();

    Perfume getPerfumeById(Long id);
    
    Perfume addPerfume(Perfume perfume);

}
