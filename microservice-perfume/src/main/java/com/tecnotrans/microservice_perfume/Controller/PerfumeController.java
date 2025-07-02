package com.tecnotrans.microservice_perfume.Controller;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Service.PerfumeService;
import com.tecnotrans.microservice_perfume.dto.PerfumeDTO;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/perfumes")
@Tag(name = "Perfumes", description = "Operations related to perfumes")
public class PerfumeController {
    
    @Autowired
    private PerfumeService perfumeService;

    @GetMapping("/listAll")
    @Operation(summary = "Get all perfumes", description = "Returns a list of all perfumes avaliables in the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "Succesful retrieval of perfume list",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = Perfume.class))),
            @ApiResponse(responseCode = "500", 
                         description = "Internal server error",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Unexpected error occurred\", \"path\": \"/api/v1/perfumes\"}")))    
                            })
    public ResponseEntity<?> getPerfumes(){
        return ResponseEntity.ok(perfumeService.getPerfumes());
    }
        
    @GetMapping("/search/{id}")
    @Operation(
        summary = "Get 1 perfume by ID", 
        description = "Returns a perfume using the ID number",
        parameters = {@Parameter(name = "id", description = "ID of the perfume to retrieve", required = true, example = "1")})
        @ApiResponses(value = {
             @ApiResponse(responseCode = "200", 
                          description = "Perfume found", 
                          content = @Content(mediaType = "application/json", 
                          schema = @Schema(implementation = Perfume.class))),
             @ApiResponse(responseCode = "404", 
                          description = "Perfume not found", 
                          content = @Content(mediaType = "application/json",
                          examples = @ExampleObject(value = "{\"message\": \"Perfume with ID: X not found\", \"status\": 404, \"timestamp\": \"2025-07-01T12:00:00\"}")))
        }
            )  
    public ResponseEntity<?> getPerfumeById(@PathVariable Long id){
        Optional<Perfume> perfume = perfumeService.getPerfumeByIdOpt(id);    
        
        if(perfume.isPresent()){
            return ResponseEntity.ok()
                        .header("mi-encabezado","valor")
                        .body(perfume.get());
        }
        else{
            //Respuesta de error con cuerpo personalizado
            Map<String,String> errorBody = new HashMap<>();
            errorBody.put("message","Perfume with ID : " + id + " not found");
            errorBody.put("status","404");
            errorBody.put("timestamp",LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorBody);
        }
    }

    @PostMapping("/create")
    @Operation(
        summary = "Creates a new perfume", 
        description = "Creates and saves a perfume using the provided details",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Perfume data to create",
            required = true,
            content = @Content(schema = @Schema(implementation = PerfumeDTO.class))
        ))
        @ApiResponses(value = {
             @ApiResponse(responseCode = "201", 
                          description = "Perfume successfully created",
                          content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = PerfumeDTO.class))),
             @ApiResponse(responseCode = "500", 
                         description = "Internal server error",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\"}")))    
                            })
    public ResponseEntity<?> addPerfume(@Valid @RequestBody PerfumeDTO perfumeDTO){
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
        } catch(Exception e){
            Map<String,String> error = new HashMap<>();
            error.put("message","Internal server error");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Updates an existing perfume", 
        description = "Updates a perfume using the provided details",
        parameters = {@Parameter(name = "id", description = "ID of the perfume to update", required = true, example = "1")},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Perfume data to update",
            required = true,
            content = @Content(schema = @Schema(implementation = PerfumeDTO.class))))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "Perfume successfully updated",
                         content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PerfumeDTO.class))),
            @ApiResponse(responseCode = "404", 
                          description = "Perfume not found", 
                          content = @Content(mediaType = "application/json",
                          examples = @ExampleObject(value = "{\"message\": \"Perfume with ID: X not found\", \"status\": 404, \"timestamp\": \"2025-07-01T12:00:00\"}")))
        }) 
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
    @Operation(
        summary = "Deletes an existing perfume", 
        description = "Deletes an existing perfume using the ID number",
        parameters = {@Parameter(name = "id", description = "ID of the perfume to delete", required = true, example = "1")}
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", 
                         description = "Perfume successfully deleted",
                         content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", 
                          description = "Perfume not found", 
                          content = @Content(mediaType = "application/json",
                          examples = @ExampleObject(value = "{\"message\": \"Perfume with ID: X not found\", \"status\": 404, \"timestamp\": \"2025-07-01T12:00:00\"}")))
        })
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            perfumeService.deletePerfumeById(id);
            return ResponseEntity.noContent().build();
        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/darPerfume/{id}")
    @Operation(summary = "Get a perfume by its ID for validation",
               description = "Returns a perfume using its ID",
               parameters = {@Parameter(name = "id", description = "Id of the perfume", required = true, example = "1")})
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Perfume found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Perfume.class))),
            @ApiResponse(responseCode = "500", 
                          description = "Internal Server Error", 
                          content = @Content(mediaType = "application/json",
                          examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-02T15:57:54.853+00:00\", \"status\": 500, \"error\": \"Internal Server Error\", \"path\": \"/api/v1/perfumes/darPerfume/15\"}")))
        })
    public ResponseEntity<?> darPerfume(@PathVariable Long id){
        System.out.println("ejecutado dar perfume");
        return ResponseEntity.ok(perfumeService.getPerfumeById(id));
    }

    @PostMapping("/adjustStock/{id}")
    @Operation(
        summary = "Adjusts the stock of a perfume to make a sale", 
        description = "Substracs stock from the given perfume ID to make a sale on sales microservice",
        parameters = {@Parameter(name = "id", description = "ID of the perfume", required = true, example = "1"),
                      @Parameter(name = "substract", description = "Amount of stock to substract", required = true, example = "4")}
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock successfully adjusted"),
            @ApiResponse(responseCode = "500", 
                          description = "Internal Server Error", 
                          content = @Content(mediaType = "application/json",
                          examples = @ExampleObject(value = "{\"message\": \"Internal Server Error\", \"status\": 500, \"timestamp\": \"2025-07-01T12:00:00\"}")))
        })
    public void adjustStock(@PathVariable Long id, @RequestParam("substract") Integer substract){
        System.out.println("STOCK TO ADJUST = " + substract);
        Perfume perfume1 = perfumeService.getPerfumeById(id);
        perfume1.setStock(perfume1.getStock() - substract);
        perfumeService.addPerfume(perfume1);
    }
}