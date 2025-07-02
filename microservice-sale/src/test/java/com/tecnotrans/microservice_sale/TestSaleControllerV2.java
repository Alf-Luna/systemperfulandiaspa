package com.tecnotrans.microservice_sale;

import com.tecnotrans.microservice_sale.Controller.SaleControllerV2;
import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Service.SaleServiceImpl;
import com.tecnotrans.microservice_sale.Assembler.SaleModelAssembler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@WebMvcTest(SaleControllerV2.class)
public class TestSaleControllerV2 {

@Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleServiceImpl saleService;

    @MockBean
    private SaleModelAssembler assembler;

    @Test
    public void testGetSalessHATEOAS() throws Exception {
        Sale sale = createTestSale();
        List<Sale> sales = Arrays.asList(sale);
        when(saleService.findAll()).thenReturn(sales);
        when(assembler.toModel(sale)).thenReturn(
                EntityModel.of(sale,
                        linkTo(methodOn(SaleControllerV2.class).getSaleById(1L)).withSelfRel(),
                        linkTo(methodOn(SaleControllerV2.class).getAllSales()).withRel("all-sales")
                )
        );

       mockMvc.perform(get("/api/v2/sales/listAll").accept(MediaTypes.HAL_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.saleList[0].id").value(sale.getId()))
        .andExpect(jsonPath("$._embedded.saleList[0]._links.self.href").exists())
        .andExpect(jsonPath("$._embedded.saleList[0]._links.all-sales.href").exists())
        .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    public void testGetIdHateOasTrue() throws Exception {
        Sale sale = createTestSale();
        when(saleService.findByIdOpt(1L)).thenReturn(Optional.of(sale));
        when(assembler.toModel(sale)).thenReturn(
                EntityModel.of(sale,
                        linkTo(methodOn(SaleControllerV2.class).getSaleById(1L)).withSelfRel(),
                        linkTo(methodOn(SaleControllerV2.class).getAllSales()).withRel("all-sales")
                )
        );

        mockMvc.perform(get("/api/v2/sales/search/1").accept(MediaTypes.HAL_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$._links.self.href").exists())
        .andExpect(jsonPath("$._links.all-sales.href").exists());
    }

    @Test
    public void testGetIdHateOasFalse() throws Exception {
        when(saleService.findByIdOpt(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/sales/search/1").accept(MediaTypes.HAL_JSON))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("No se encontró una venta con la ID: 1"));
    }

    private Sale createTestSale() {
        return Sale.builder()
                .id(1L)
                .qty(2)
                .idPerfume(5L)
                .idUser(10L)
                .date(LocalDateTime.of(2025, 7, 1, 12, 0))
                .build();
    }
}
