package com.tecnotrans.microservice_sale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecnotrans.microservice_sale.Client.PerfumeClient;
import com.tecnotrans.microservice_sale.Client.UserClient;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Repository.ISaleRepository;
import com.tecnotrans.microservice_sale.Service.SaleServiceImpl;
import com.tecnotrans.microservice_sale.dto.PerfumeDTO;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @InjectMocks
    private SaleServiceImpl saleService;

    @Mock
    private ISaleRepository saleRepository;

    @Mock
    private PerfumeClient perfumeClient;

    @Mock
    private UserClient userClient;
    
    @Test
    public void findAll(){
        when(saleRepository.findAll()).thenReturn(List.of(createTestSale()));

        List<Sale> sales = saleService.findAll();
        
        assertNotNull(sales);
        assertEquals(1, sales.size());
    }
    
    @Test
    public void findById(){
        Long code = 1l;
        Sale sale = createTestSale();

        when(saleRepository.findById(code)).thenReturn(Optional.of(sale));

        Sale foundSale = saleService.findById(code);

        assertNotNull(foundSale);
        assertEquals(code, foundSale.getId());
    }

    @Test
    public void findByIdOpt(){
        Long code = 1l;
        Sale sale = createTestSale();

        when(saleRepository.findById(code)).thenReturn(Optional.of(sale));

        Optional<Sale> foundSale = saleService.findByIdOpt(code);

        assertNotNull(foundSale);
        assertEquals(code, foundSale.get().getId());
    }

    @Test
    public void save(){
        Sale sale = createTestSale();

        when(saleRepository.save(sale)).thenReturn(sale);

        Sale savedSale = saleService.save(sale);

        assertNotNull(savedSale);
        assertEquals(1L, savedSale.getIdUser());
    }

    @Test
    public void deleteById(){
        Long codigo = 1l;

        doNothing().when(saleRepository).deleteById(codigo);

        saleService.deleteById(codigo);

        verify(saleRepository, times(1)).deleteById(codigo);
    }

    @Test
    public void getPerfume(){
        Long code = 1l;

        PerfumeDTO perfumeDTO = PerfumeDTO.builder()
        .id(code)
        .name("Imperial Majesty")
        .stock(10)
        .price(50.5F)
        .brand("Clive Christian").build();

        when(perfumeClient.dameUnPerfume(code)).thenReturn(perfumeDTO);

        PerfumeDTO foundPerfumeDTO = saleService.dameUnPerfume(code);

        assertNotNull(foundPerfumeDTO);
        assertEquals(code, foundPerfumeDTO.getId());
    }

    @Test
    public void testUpdateStockDueToSale(){
        Long codigo = 1l;

        doNothing().when(perfumeClient).updateStockDueToSale(codigo, 1);

        saleService.updateStockDueToSale(codigo, 1);

        verify(perfumeClient, times(1)).updateStockDueToSale(codigo, 1);
    }

    @Test
    public void testValidateUser(){
        Long code = 1L;

        when(userClient.validateUser(code)).thenReturn(true);

        Boolean result = saleService.validateUser(code);

        assertTrue(result);
    }

    private Sale createTestSale() {
        return Sale.builder()
        .id(1L)
        .idPerfume(1L)
        .idUser(1L)
        .date(LocalDateTime.now()).build();    
    }
}
