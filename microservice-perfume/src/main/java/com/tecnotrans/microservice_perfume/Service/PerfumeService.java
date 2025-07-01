package com.tecnotrans.microservice_perfume.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Repository.PerfumeRepository;

@Service
public class PerfumeService implements IPerfumeService{
    @Autowired
    PerfumeRepository perfumeRepository;

    @Override
    public List<Perfume> getPerfumes(){
        return perfumeRepository.findAll();
    }

    @Override
    public Perfume getPerfumeById(Long id){
        return perfumeRepository.findById(id).get();
    }

    public Optional<Perfume> getPerfumeByIdOpt(Long id){
        return perfumeRepository.findById(id);
    }

    @Override
    public Perfume addPerfume(Perfume perfume) {
        perfumeRepository.save(perfume);
        return perfume;
    }

    public void updatePerfume(Perfume perfume) {
        perfumeRepository.save(perfume);
    }

    public String deletePerfumeById(Long id) {
        perfumeRepository.deleteById(id);
        return "";
    }

    @Override
    public Perfume accessPerfume(Long id) {
        return getPerfumeById(id);
    }

    //dame un numero
}
