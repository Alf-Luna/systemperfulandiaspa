package com.tecnotrans.microservice_perfume.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecnotrans.microservice_perfume.Assembler.PerfumeModelAssembler;
import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Service.PerfumeService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2/perfumes")
@Tag(name = "Perfume V2", description = "API for managing perfumes with HATEOAS support")
public class PerfumeControllerV2 {

    @Autowired
    private PerfumeService perfumeService;

    @Autowired
    private PerfumeModelAssembler assembler;

    @GetMapping(value = "/listAll", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Get all perfumes", 
        description = "Returns a list of all perfumes avaliables in the catalog with HATEOAS links")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "Succesful retrieval of perfume list",
                         content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = Perfume.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Eau de Parfum\", \"stock\": 100, \"price\": 59.99, \"brand\": \"Chanel\", \"_links\": {\"self\": {\"href\": \"http://localhost:8090/api/v2/perfumes/search/1\"}, \"all-perfumes\": {\"href\": \"http://localhost:8090/api/v2/perfumes/listAll\"}}}"))),
            @ApiResponse(responseCode = "500", 
                         description = "Internal server error",
                         content = @Content(
                            mediaType = "application/hal+json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\"}")))    
                            })
    public CollectionModel<EntityModel<Perfume>> getPerfumes() {
        List<EntityModel<Perfume>> perfumes = perfumeService.getPerfumes().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(perfumes,
                    linkTo(methodOn(PerfumeControllerV2.class).getPerfumes()).withSelfRel());
    }

    @GetMapping(value = "/search/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Get 1 perfume by ID", 
        description = "Returns a perfume using the ID number with HATEOAS links",
        responses = {
            @ApiResponse(responseCode = "200", 
                         description = "Perfume found",
                         content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = Perfume.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Eau de Parfum\", \"stock\": 100, \"price\": 59.99, \"brand\": \"Chanel\", \"_links\": {\"self\": {\"href\": \"http://localhost:8090/api/v2/perfumes/search/1\"}, \"all-perfumes\": {\"href\": \"http://localhost:8090/api/v2/perfumes/listAll\"}}}"))),
            @ApiResponse(responseCode = "404", 
                         description = "Perfume not found",
                         content = @Content(mediaType = "application/hal+json",
                         examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"message\": \"Perfume with ID: X not found\"}")))
        }
            ) 
    public ResponseEntity<?> getPerfumeById(@PathVariable Long id) {
        Optional<Perfume> perfume = perfumeService.getPerfumeByIdOpt(id);

    if (perfume.isPresent()) {
        EntityModel<Perfume> model = assembler.toModel(perfume.get());
        return ResponseEntity.ok()
                .header("mi-encabezado", "valor")
                .body(model);
    } else {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("message", "Perfume with ID : " + id + " not found");
        errorBody.put("status", "404");
        errorBody.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }
        }


}
