package com.tecnotrans.microservice_perfume.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Repository.PerfumeRepository;

@Service
public class PerfumeService {
    @Autowired
    PerfumeRepository perfumeRepository;

    public List<Perfume> getPerfumes(){
        return perfumeRepository.findAll();
    }

    public Perfume getPerfumeById(Long id){
        return perfumeRepository.findById(id).get();
    }

    public Optional<Perfume> getPerfumeByIdOpt(Long id){
        return perfumeRepository.findById(id);
    }

    public void addPerfume(Perfume perfume) {
        perfumeRepository.save(perfume);
    }

    public void updateBook(Perfume perfume) {
        perfumeRepository.save(perfume);
    }

    public void deleteBookById(Long id) {
        perfumeRepository.deleteById(id);
    }
}
