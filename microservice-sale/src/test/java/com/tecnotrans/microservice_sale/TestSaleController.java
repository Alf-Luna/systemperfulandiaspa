package com.tecnotrans.microservice_sale;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.tecnotrans.microservice_sale.Controller.SaleController;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Service.SaleServiceImpl;
import com.tecnotrans.microservice_sale.dto.PerfumeDTO;
import com.tecnotrans.microservice_sale.dto.SaleDTO;

import net.minidev.json.JSONObject;

@WebMvcTest(SaleController.class)
public class TestSaleController {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SaleServiceImpl saleService;

    @Test
    public void testFindAllSales() throws Exception{
        when(saleService.findAll()).thenReturn(Arrays.asList(createTestSale()));
        
        mockMvc.perform(get("/api/v1/sales/listAll")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].idPerfume").value("1"));
    }

    @Test
    public void testFindById() throws Exception{
        when(saleService.findByIdOpt(1l)).thenReturn(Optional.of(createTestSale()));
        
        mockMvc.perform(get("/api/v1/sales/search/1")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("idPerfume").value("1"));
    }

    @Test
    public void testFailFindById() throws Exception{
        when(saleService.findByIdOpt(1l)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/v1/sales/search/1")).andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testSave() throws Exception{
        Sale sale = createTestSale();

        when(saleService.save(sale)).thenReturn(sale);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());
        jsonUser.put("qty", sale.getQty());

        mockMvc.perform(post("/api/v1/sales/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("idPerfume").value(sale.getIdPerfume().toString()));
    }

    @Test
    public void testSaveConflict() throws Exception{
        Sale sale = createTestSale();

        when(saleService.save(sale)).thenThrow(new DataIntegrityViolationException("Dato Repetido"));

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());
        jsonUser.put("qty", sale.getQty());

        mockMvc.perform(post("/api/v1/sales/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isConflict());
    }

    @Test
    public void testUpdate() throws Exception{
        Sale sale = createTestSale();

        when(saleService.save(sale)).thenReturn(sale);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());
        jsonUser.put("qty", sale.getQty());

        mockMvc.perform(put("/api/v1/sales/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("idPerfume").value(sale.getIdPerfume().toString()));
    }

    @Test
    public void testUpdateNotFound() throws Exception{
        Sale sale = createTestSale();

        when(saleService.save(sale)).thenReturn(sale);

        JSONObject jsonUser = new JSONObject();

        mockMvc.perform(put("/api/v1/sales/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception{
        when(saleService.deleteById(1l)).thenReturn("");

        mockMvc.perform(delete("/api/v1/sales/1"))
        .andDo(print())
        .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNotFound() throws Exception{
        when(saleService.deleteById(1l)).thenThrow(new IllegalArgumentException("id invalida"));

        mockMvc.perform(delete("/api/v1/sales/1"))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testInvalidUserMakeSale() throws Exception{
        Sale sale = createTestSale();
        when(saleService.validateUser(1l)).thenReturn(false);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());

        mockMvc.perform(post("/api/v1/sales/makeSale")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andExpect(status().isNotFound());
    }

    @Test
    public void testInvalidQtyMakeSale() throws Exception{
        Sale sale = createTestSale();
        sale.setQty(0);
        when(saleService.validateUser(1l)).thenReturn(true);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());

        mockMvc.perform(post("/api/v1/sales/makeSale")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andExpect(status().isConflict());
    }

    @Test
    public void testInvalidStockMakeSale() throws Exception{
        Sale sale = createTestSale();

        PerfumeDTO perfumeDTO = createTestPerfumeDto();
        perfumeDTO.setStock(0);
        when(saleService.validateUser(1l)).thenReturn(true);
        when(saleService.dameUnPerfume(1l)).thenReturn(perfumeDTO);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());
        jsonUser.put("qty", sale.getQty());

        mockMvc.perform(post("/api/v1/sales/makeSale")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andExpect(status().isConflict());
    }

    @Test
    public void testInvalidPerfumeMakeSale() throws Exception{
        Sale sale = createTestSale();
        when(saleService.validateUser(1l)).thenReturn(true);
        when(saleService.dameUnPerfume(1l)).thenThrow(new IllegalArgumentException("id invalida"));

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());
        jsonUser.put("qty", sale.getQty());

        mockMvc.perform(post("/api/v1/sales/makeSale")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andExpect(status().isNotFound());
    }

    @Test
    public void testMakeSale() throws Exception{
        Sale sale = createTestSale();

        PerfumeDTO perfumeDTO = createTestPerfumeDto();
        when(saleService.validateUser(1l)).thenReturn(true);
        when(saleService.dameUnPerfume(1l)).thenReturn(perfumeDTO);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());
        jsonUser.put("qty", sale.getQty());

        mockMvc.perform(post("/api/v1/sales/makeSale")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andExpect(status().isCreated());
    }

    @Test
    public void testDataIntegrityViolationMakeSale() throws Exception{
        Sale sale = createTestSale();

        PerfumeDTO perfumeDTO = createTestPerfumeDto();
        when(saleService.validateUser(1l)).thenThrow(new DataIntegrityViolationException(""));
        when(saleService.dameUnPerfume(1l)).thenReturn(perfumeDTO);
        when(saleService.save(sale)).thenReturn(sale);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", sale.getId().toString());
        jsonUser.put("idPerfume", sale.getIdPerfume().toString());
        jsonUser.put("idUser", sale.getIdUser().toString());
        jsonUser.put("date", sale.getDate().toString());
        jsonUser.put("qty", sale.getQty());

        mockMvc.perform(post("/api/v1/sales/makeSale") 
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andExpect(status().isConflict());
    }

    private Sale createTestSale() {
        return Sale.builder()
        .id(1L)
        .qty(1)
        .idPerfume(1L)
        .idUser(1L)
        .date(LocalDateTime.now()).build();    
    }

    private PerfumeDTO createTestPerfumeDto(){
        return PerfumeDTO.builder()
        .id(1l)
        .name("Spirited Away")
        .stock(2)
        .price(2001)
        .brand("Ghibli").build();
    }

    private SaleDTO createTestSaleDto(){
        return SaleDTO.builder()
        .id(1L)
        .idPerfume(1L)
        .idUser(1L)
        .date(LocalDateTime.now()).build();   
    }
}
