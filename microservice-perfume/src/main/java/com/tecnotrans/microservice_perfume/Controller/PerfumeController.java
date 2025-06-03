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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Service.PerfumeService;
import com.tecnotrans.microservice_perfume.dto.PerfumeDTO;

import jakarta.validation.Valid;

@RestController
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
    public ResponseEntity<?> getById(@PathVariable Long id){
        Optional<Perfume> perfume = perfumeService.getPerfumeByIdOpt(id);    
        
        if(perfume.isPresent()){

            //Preguntarle al profe porque se crea este dto si no se ocupa en ninguna parte.
            //Quizas se deberia poner en el body del return?
            PerfumeDTO dto = new PerfumeDTO();
            dto.setId(perfume.get().getId());
            dto.setName(perfume.get().getName());
            dto.setStock(perfume.get().getStock());
            dto.setPrice(perfume.get().getPrice());
            dto.setBrand(perfume.get().getBrand());

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

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody PerfumeDTO perfumeDTO){
        try{  
        Perfume perfume = new Perfume();
        perfume.setId(perfumeDTO.getId());
        perfume.setName(perfumeDTO.getName());
        perfume.setStock(perfumeDTO.getStock());
        perfume.setPrice(perfumeDTO.getPrice());
        perfume.setBrand(perfumeDTO.getBrand());

        perfumeService.addPerfume(perfume);
        //Por que se crea una copia del paciente que ya se creo? En vez de ocupar directamente el ya creado
        //Paciente pacienteGuardado = pacienteService.save(paciente);

        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(perfume.getId());
        dto.setName(perfume.getName());
        dto.setStock(perfume.getStock());
        dto.setPrice(perfume.getPrice());
        dto.setBrand(perfume.getBrand());

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(perfume.getId())
            .toUri();

        return ResponseEntity.created(location).body(dto);
        }
        catch(DataIntegrityViolationException e){
            //Ejemplo: Error si hay un campo único duplicado (ej: email repetido)
            Map<String,String> error = new HashMap<>();
            error.put("message","El email ya está registrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);//Error 409
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfumeDTO> update(@PathVariable Long id, @RequestBody PerfumeDTO perfumeDTO){
        try{
            Perfume perfume = new Perfume();
            perfume.setId(perfumeDTO.getId());
            perfume.setName(perfumeDTO.getName());
            perfume.setStock(perfumeDTO.getStock());
            perfume.setPrice(perfumeDTO.getPrice());
            perfume.setBrand(perfumeDTO.getBrand());

            perfumeService.addPerfume(perfume);

            PerfumeDTO dto = new PerfumeDTO();
            dto.setId(perfume.getId());
            dto.setName(perfume.getName());
            dto.setStock(perfume.getStock());
            dto.setPrice(perfume.getPrice());
            dto.setBrand(perfume.getBrand());

            return ResponseEntity.ok(dto);
        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    //TODO eliminar

    //TODOOOOOOOOOO
    //localhost:8090/api/v1/student/search-by-course/1
    //@GetMapping("/search-by-course/{courseId}")
    //public ResponseEntity<?> findByIdCourse(@PathVariable Long courseId){         
    //     return ResponseEntity.ok(perfumeService.findByIdCourse(courseId));
    //}
}
