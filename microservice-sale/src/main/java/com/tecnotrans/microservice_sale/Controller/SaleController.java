package com.tecnotrans.microservice_sale.Controller;

///import com.netflix.discovery.converters.Auto;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Service.ISaleService;
import com.tecnotrans.microservice_sale.Service.SaleServiceImpl;
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
///import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/v1/sales")
public class SaleController {

    @Autowired
    private SaleServiceImpl saleService;

    @Autowired
    private ISaleService iSaleService;

    @GetMapping("/listAll")
    public ResponseEntity<?> findAllSales() {
        return ResponseEntity.ok(saleService.findAll());
    }
    
    @GetMapping("/search/{id}")
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
    
    @PostMapping("/create")
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

    @DeleteMapping
    public String deleteById(@PathVariable Long id){
        saleService.deleteById(id);
        return "Eliminado";
    }

    @GetMapping("/perfumeid/{idCourse}")
    public ResponseEntity<?> getPerById(@PathVariable Long id){
        return ResponseEntity.ok(iSaleService.accessPerfumeById(id));
    }

    @GetMapping("/numero/{id}")
    public int giveMeYourNummerBBY(@PathVariable int id){
        return saleService.nummer(id);
    }
}
