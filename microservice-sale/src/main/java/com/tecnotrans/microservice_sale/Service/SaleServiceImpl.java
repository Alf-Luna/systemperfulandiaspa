package com.tecnotrans.microservice_sale.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tecnotrans.microservice_sale.Client.PerfumeClient;
import com.tecnotrans.microservice_sale.Client.UserClient;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Repository.ISaleRepository;
import com.tecnotrans.microservice_sale.dto.PerfumeDTO;
//import com.tecnotrans.microservice_sale.http.Response.AccessPerfumeByIdResponse;

import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements ISaleService{

    @Autowired
    private ISaleRepository iSaleRepository;

    @Autowired
    private PerfumeClient perfumeClient; 

    @Autowired
    private UserClient userClient;

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
    public String deleteById(Long id){
        iSaleRepository.deleteById(id);
        return "";
    }

    /*@Override
    public AccessPerfumeByIdResponse accessPerfumeById(Long idPerfume){
        PerfumeDTO perfumeDTO = perfumeClient.accessPerfumeById(idPerfume);

        return AccessPerfumeByIdResponse.builder()
                .perfumeName(perfumeDTO.getName())
                .stock(perfumeDTO.getStock())
                .build();
    }*/

    public PerfumeDTO dameUnPerfume(Long id){
        System.out.println("dame un perfume saleservice");
        return perfumeClient.dameUnPerfume(id);
    }

    public void updateStockDueToSale(Long id, Integer substract){
        System.out.println("Sale service start. Subtract = " + substract);
        perfumeClient.updateStockDueToSale(id, substract);
    }

    public boolean validateUser(Long idUser) {
        return userClient.validateUser(idUser);
    }
}
