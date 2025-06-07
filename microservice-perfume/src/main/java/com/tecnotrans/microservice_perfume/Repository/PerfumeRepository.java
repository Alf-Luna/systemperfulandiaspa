package com.tecnotrans.microservice_perfume.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecnotrans.microservice_perfume.Model.Perfume;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long>{

}
