package com.tecnotrans.microservice_perfume.Controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Service.PerfumeService;
import com.tecnotrans.microservice_perfume.dto.PerfumeDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/perfumes")
public class PerfumeController {
    
    @Autowired
    private PerfumeService perfumeService;

    @GetMapping("/listAll")
    public ResponseEntity<?> getPerfumes(){
        return ResponseEntity.ok(perfumeService.getPerfumes());
    }
        
    @GetMapping("/search/{id}")    
    public ResponseEntity<?> getById(@PathVariable Long id){
        Optional<Perfume> perfume = perfumeService.getPerfumeByIdOpt(id);    
        
        if(perfume.isPresent()){
            return ResponseEntity.ok()
                        .header("mi-encabezado","valor")
                        .body(perfume.get());
        }
        else{
            //Respuesta de error con cuerpo personalizado
            Map<String,String> errorBody = new HashMap<>();
            errorBody.put("message","No se encontró un perfume con esa ID: " + id);
            errorBody.put("status","404");
            errorBody.put("timestamp",LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorBody);
        }
    }

    
    @ResponseStatus(HttpStatus.CREATED)
    public void savePerfume(@RequestBody Perfume perfume){
        perfumeService.addPerfume(perfume);
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@Valid @RequestBody PerfumeDTO perfumeDTO){
        try{  
            Perfume perfume = new Perfume();
            perfume.setId(perfumeDTO.getId());
            perfume.setName(perfumeDTO.getName());
            perfume.setStock(perfumeDTO.getStock());
            perfume.setPrice(perfumeDTO.getPrice());
            perfume.setBrand(perfumeDTO.getBrand());

            Perfume perfumeSaved = perfumeService.addPerfume(perfume);

            PerfumeDTO dto = new PerfumeDTO();
            dto.setId(perfumeSaved.getId());
            dto.setName(perfumeSaved.getName());
            dto.setStock(perfumeSaved.getStock());
            dto.setPrice(perfumeSaved.getPrice());
            dto.setBrand(perfumeSaved.getBrand());

            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(perfume.getId())
                .toUri();

            return ResponseEntity.created(location).body(dto);
        } catch(DataIntegrityViolationException e){
            Map<String,String> error = new HashMap<>();
            error.put("message","El email ya está registrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfumeDTO> update(@PathVariable Long id, @RequestBody PerfumeDTO perfumeDTO){
        try{
            Perfume perfume = new Perfume();
            perfume.setId(perfumeDTO.getId());
            perfume.setName(perfumeDTO.getName());
            perfume.setStock(perfumeDTO.getStock());
            perfume.setPrice(perfumeDTO.getPrice());
            perfume.setBrand(perfumeDTO.getBrand());

            Perfume perfumeUpdated = perfumeService.addPerfume(perfume);

            PerfumeDTO dto = new PerfumeDTO();
            dto.setId(perfumeUpdated.getId());
            dto.setName(perfumeUpdated.getName());
            dto.setStock(perfumeUpdated.getStock());
            dto.setPrice(perfumeUpdated.getPrice());
            dto.setBrand(perfumeUpdated.getBrand());

            return ResponseEntity.ok(dto);

        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            perfumeService.deletePerfumeById(id);
            return ResponseEntity.noContent().build();
        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    /*@GetMapping("/access-perfume-by-id/{id}")
    public ResponseEntity<?> getPerfumeById(@PathVariable Long id){         
        return ResponseEntity.ok(perfumeService.getPerfumeById(id));
    }*/

    @GetMapping("/darPefume/{id}")
    public ResponseEntity<?> darPerfume(@PathVariable Long id){
        System.out.println("ejecutado dar perfume");
        return ResponseEntity.ok(perfumeService.getPerfumeById(id));
    }

    @PostMapping("/adjustStock/{id}")
    public void adjustStock(@PathVariable Long id, @RequestParam("substract") Integer substract){
        System.out.println("STOCK TO ADJUST = " + substract);
        Perfume perfume1 = perfumeService.getPerfumeById(id);
        perfume1.setStock(perfume1.getStock() - substract);
        perfumeService.addPerfume(perfume1);
    }
}
