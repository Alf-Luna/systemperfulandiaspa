package com.tecnotrans.microservice_sale.Controller;

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


import com.tecnotrans.microservice_sale.Assembler.SaleModelAssembler;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Service.SaleServiceImpl;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v2/sales")
@Tag(name = "Sale V2", description = "API for managing sales with HATEOAS support")
public class SaleControllerV2 {

@Autowired
private SaleServiceImpl saleService;

@Autowired
private SaleModelAssembler assembler;

@GetMapping(value = "/listAll", produces = MediaTypes.HAL_JSON_VALUE)
@Operation(
        summary = "Get all sales", 
        description = "Returns a list of all recorded sales in the system with HATEOAS links")
        @ApiResponses( value = {
            @ApiResponse(responseCode = "200", 
                         description = "Succesful retrieval of perfume list",
                         content = @Content(mediaType = "application/hal.json",
                            schema = @Schema(implementation = Sale.class),
                            examples = @ExampleObject(value = "{\"id\": 100, \"date\": \"2025-06-30T15:45:00\", \"qty\": 2, \"idPerfume\": 5, \"idUser\": 42, \"_links\": {\"self\": {\"href\": \"http://localhost:9090/api/v1/sales/search/100\"}, \"all-sales\": {\"href\": \"http://localhost:9090/api/v1/sales/listAll\"}}}"))),
            @ApiResponse(responseCode = "500", 
                         description = "Internal server error",
                         content = @Content(
                            mediaType = "application/hal.json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\"}")))
        }
            )
    public CollectionModel<EntityModel<Sale>> findAll() {
        List<EntityModel<Sale>> sales = saleService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(sales,
                    linkTo(methodOn(SaleControllerV2.class).findAll()).withSelfRel());
                }


@GetMapping("/search/{id}")
@Operation(
        summary = "Get 1 sale by ID", 
        description = "Returns a sale using the ID number with HATEOAS links",
        responses = {
            @ApiResponse(responseCode = "200", 
                         description = "Sale found",
                         content = @Content(mediaType = "application/hal.json",
                            schema = @Schema(implementation = Sale.class),
                            examples = @ExampleObject(value = "{\"id\": 100, \"date\": \"2025-06-30T15:45:00\", \"qty\": 2, \"idPerfume\": 5, \"idUser\": 42, \"_links\": {\"self\": {\"href\": \"http://localhost:9090/api/v1/sales/search/100\"}, \"all-sales\": {\"href\": \"http://localhost:9090/api/v1/sales/listAll\"}}}") )),
            @ApiResponse(responseCode = "404", 
                         description = "Perfume not found",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"message\": \"Sale with ID: X not found\"}") ))
        }
            ) 
    public ResponseEntity<?> findByID(@PathVariable Long id) {
        Optional<Sale> sale = saleService.findByIdOpt(id);    
        
        if(sale.isPresent()){
            return ResponseEntity.ok()
                        .header("mi-encabezado","valor")
                        .body(sale.get());
        }
        else{
            //Respuesta de error con cuerpo personalizado
            Map<String,String> errorBody = new HashMap<>();
            errorBody.put("message","No se encontró una venta con la ID: " + id);
            errorBody.put("status","404");
            errorBody.put("timestamp",LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorBody);
        }
    }
            }

