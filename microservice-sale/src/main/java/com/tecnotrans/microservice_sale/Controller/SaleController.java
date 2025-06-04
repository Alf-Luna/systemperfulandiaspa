package com.tecnotrans.microservice_sale.Controller;

///import com.netflix.discovery.converters.Auto;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Service.ISaleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
///import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/v1/sales")
public class SaleController {

    @Autowired
    private ISaleService saleService;

    @GetMapping("/listAll")
    public ResponseEntity<?> findAllSales() {
        return ResponseEntity.ok(saleService.findAll());
    }
    
    @GetMapping("/search/{id}")
    public ResponseEntity<?> findByID(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }
    
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Sale sale){
        saleService.save(sale);
    }

    @DeleteMapping
    public String deleteById(@PathVariable Long id){
        saleService.deleteById(id);
        return "Eliminado";
    }

    /*@GetMapping("/search-student/{idCourse}")
    public ResponseEntity<?> findStudentsByIdCourse(@PathVariable Long idCourse){
        return ResponseEntity.ok(courseService.findStudentsByIdCourse(idCourse));
    } */ ///aún no tenemos un equivalente para esto


}
