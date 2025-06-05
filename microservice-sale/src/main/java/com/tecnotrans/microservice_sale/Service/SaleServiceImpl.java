package com.tecnotrans.microservice_sale.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tecnotrans.microservice_sale.Client.PerfumeClient;
///import com.microservice.sale.client.PerfumeClient; Esto aún no existe en nuestro proyecto
///mport com.microservice.course.dto.StudentDTO;
///import com.microservice.course.http.response.StudentByCourseResponse;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Repository.ISaleRepository;
import com.tecnotrans.microservice_sale.dto.PerfumeDTO;
import com.tecnotrans.microservice_sale.http.Response.AccessPerfumeByIdResponse;

import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements ISaleService{

    @Autowired
    private ISaleRepository iSaleRepository;

    @Autowired
    private PerfumeClient perfumeClient; 

    @Override
    public List<Sale> findAll() {
        return (List<Sale>) iSaleRepository.findAll();
    }

    @Override
    public Sale findById(Long id) {
        return iSaleRepository.findById(id).orElseThrow();
    }

    public Optional<Sale> findByIdOpt(Long id){
        return iSaleRepository.findById(id);
    }

    @Override
    public Sale save(Sale sale) {
        return iSaleRepository.save(sale);
    }

    @Override
    public void deleteById(Long id){
        iSaleRepository.deleteById(id);
    }

    @Override
    public AccessPerfumeByIdResponse accessPerfumeById(Long idPerfume){
        PerfumeDTO perfumeDTO = perfumeClient.accessPerfumeById(idPerfume);

        return AccessPerfumeByIdResponse.builder()
                .perfumeName(perfumeDTO.getName())
                .stock(perfumeDTO.getStock())
                .build();
    }
}
