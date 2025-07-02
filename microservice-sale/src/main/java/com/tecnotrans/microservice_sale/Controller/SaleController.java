package com.tecnotrans.microservice_sale.Controller;

import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Service.SaleServiceImpl;
import com.tecnotrans.microservice_sale.dto.PerfumeDTO;
import com.tecnotrans.microservice_sale.dto.SaleDTO;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@RequestMapping("api/v1/sales")
@Tag(name = "Sales", description = "API for managing sales")
public class SaleController {

    @Autowired
    private SaleServiceImpl saleService;

    @GetMapping("/listAll")
    @Operation(
        summary = "Get all sales",
        description = "Retrieves a list of all recorded sales in the system")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "Sales successfully retrieved",
                         content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sale.class))),
            @ApiResponse(responseCode = "500", 
                         description = "Internal server error",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Unexpected error occurred\", \"path\": \"/api/v1/perfumes\"}")
                         ))
        }
    )
    public ResponseEntity<?> findAllSales() {
        return ResponseEntity.ok(saleService.findAll());
    }
    
    @GetMapping("/search/{id}")
    @Operation(
        summary = "Get sale by ID number",
        description = "Retrieves a sale by its ID",
        parameters = {@Parameter(name = "id", description = "ID of the sale to retrieve", required = true, example = "1")})
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "Sale found",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = Sale.class))),
            @ApiResponse(responseCode = "404", 
                         description = "Sale not found",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"message\": \"No se encontró una venta con la ID: X\"}")))
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
    
    //solo para agregar ventas realizadas previamente. No se deberia usar
    @PostMapping("/create")
    @Operation(
        summary = "Create a new sale",
        description = "Creates a new sale",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Sale data to create",
            required = true,
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SaleDTO.class))
        ))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", 
                         description = "Sale successfully created",
                         content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SaleDTO.class))),
            @ApiResponse(responseCode = "500", 
                         description = "Internal Server Error",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\"}")))
        }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@RequestBody SaleDTO saleDTO){
        try{  
        Sale sale = new Sale();
        sale.setId(saleDTO.getId());
        sale.setDate(saleDTO.getDate());
        sale.setQty(saleDTO.getQty());
        sale.setIdPerfume(saleDTO.getIdPerfume());
        sale.setIdUser(saleDTO.getIdUser());

        Sale perfumeSaved = saleService.save(sale);

        SaleDTO dto = new SaleDTO();
        dto.setId(perfumeSaved.getId());
        dto.setDate(perfumeSaved.getDate());
        dto.setQty(perfumeSaved.getQty());
        dto.setIdPerfume(perfumeSaved.getIdPerfume());
        dto.setIdUser(perfumeSaved.getIdUser());

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(sale.getId())
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

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing sale",
        description = "Updates a sale using its ID and new details",
        parameters = {@Parameter(name = "id", description = "ID of the sale to update", required = true, example = "1")},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Sale data to update",
            required = true,
            content = @Content(schema = @Schema(implementation = SaleDTO.class))))
        
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "Sale successfully updated",
                         content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SaleDTO.class))),
            @ApiResponse(responseCode = "404", 
                         description = "Sale not found",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"message\": \"No se encontró una venta con la ID: X\"}")))
        }
    )
    public ResponseEntity<SaleDTO> update(@PathVariable Long id, @RequestBody SaleDTO saleDTO){
        try{
            Sale sale = new Sale();
            sale.setId(saleDTO.getId());
            sale.setDate(saleDTO.getDate());
            sale.setQty(saleDTO.getQty());
            sale.setIdPerfume(saleDTO.getIdPerfume());
            sale.setIdUser(saleDTO.getIdUser());

            Sale saleUpdated = saleService.save(sale);

            SaleDTO dto = new SaleDTO();
            dto.setId(saleUpdated.getId());
            dto.setDate(saleUpdated.getDate());
            dto.setQty(saleUpdated.getQty());
            dto.setIdPerfume(saleUpdated.getIdPerfume());
            dto.setIdUser(saleUpdated.getIdUser());

            return ResponseEntity.ok(dto);

        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a sale",
        description = "Deletes a sale by its ID",
        parameters = {@Parameter(name = "id", description = "ID of the sale to delete", required = true, example = "1")}
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "Sale successfully deleted",
                         content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", 
                         description = "Sale not found",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"message\": \"No se encontró una venta con la ID: X\"}")))
        }
    )
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        try{
            saleService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/makeSale")
    @Operation(
        summary = "Make a new sale",
        description = "Processes a new sale, validates user, stock, and updates inventory",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Sale information to be processed",
            required = true,
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = SaleDTO.class),
            examples = @ExampleObject(value = "{\"id\": 1, \"date\": \"2025-07-01T12:34:56\", \"qty\": 2, \"idPerfume\": 3, \"idUser\": 5}"))
        ))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", 
                         description = "Sale completed successfully",
                         content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SaleDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"date\": \"2025-07-01T12:34:56\", \"qty\": 2, \"idPerfume\": 3, \"idUser\": 5}")
                         )),
            @ApiResponse(responseCode = "404", 
                         description = "User or perfume not found",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No se ha encontrado un usuario válido\"}")
                         )),
            @ApiResponse(responseCode = "409", 
                         description = "Not enough stock or invalid quantity",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No hay stock suficiente para completar su compra\"}")
                         ))
        }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> makeSale(@RequestBody SaleDTO saleDTO){
        try{  
            Sale sale = new Sale();
            sale.setId(saleDTO.getId());
            sale.setDate(java.time.LocalDateTime.now());
            sale.setQty(saleDTO.getQty());
            sale.setIdPerfume(saleDTO.getIdPerfume());
            sale.setIdUser(saleDTO.getIdUser());
            System.out.println("Sale GETQTY = " + sale.getQty());

            if (!saleService.validateUser(sale.getIdUser())){
                Map<String,String> error = new HashMap<>();
                error.put("message","No se ha encontrado un usuario valido");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            try{
                PerfumeDTO perfumeToBuy = saleService.dameUnPerfume(sale.getIdPerfume());
                System.out.println("Creado perfumetobuy exitosamente");
                if (sale.getQty() < 1){
                    Map<String,String> error = new HashMap<>();
                    error.put("message","El stock ingresado es invalido");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                }
                if (sale.getQty() <= perfumeToBuy.getStock()){
                    System.out.println("Checked if stock is sufficient");
                    saleService.updateStockDueToSale(sale.getIdPerfume(), sale.getQty());
                } else{
                    Map<String,String> error = new HashMap<>();
                    error.put("message","No hay stock suficiente para completar su compra");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                }

            } catch(Exception e){
                Map<String,String> error = new HashMap<>();
                error.put("message","No se ha encontrado un perufme valido");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sale.getId())
                .toUri();

            return ResponseEntity.created(location).body(saleDTO);
        }
        catch(DataIntegrityViolationException e){
            Map<String,String> error = new HashMap<>();
            error.put("message","Dato Invalido");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }
}
