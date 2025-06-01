package com.tecnotrans.microservice_perfume.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecnotrans.microservice_perfume.Model.Perfume;

public interface PerfumeRepository extends JpaRepository<Perfume, Long>{

}
