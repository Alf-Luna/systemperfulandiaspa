package com.tecnotrans.microservice_perfume.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Service.PerfumeService;

public class PerfumeController {
    
    @Autowired
    private PerfumeService perfumeService;
    
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void savePerfume(@RequestBody Perfume perfume){
        perfumeService.addPerfume(perfume);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getPerfumes(){
        return ResponseEntity.ok(perfumeService.getPerfumes());
    }
    

    @GetMapping("/search/{id}")    
    public ResponseEntity<?> findById(@PathVariable Long id){
        return ResponseEntity.ok(perfumeService.getPerfumeById(id));
    }

    //TODOOOOOOOOOO
    //localhost:8090/api/v1/student/search-by-course/1
    //@GetMapping("/search-by-course/{courseId}")
    //public ResponseEntity<?> findByIdCourse(@PathVariable Long courseId){         
    //     return ResponseEntity.ok(perfumeService.findByIdCourse(courseId));
    //}
}
