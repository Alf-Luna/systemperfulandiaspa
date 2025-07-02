package com.tecnotrans.microservice_perfume;

import com.tecnotrans.microservice_perfume.Controller.PerfumeControllerV2;
import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Service.PerfumeService;
import com.tecnotrans.microservice_perfume.Assembler.PerfumeModelAssembler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@WebMvcTest(PerfumeControllerV2.class)
public class PerfumeControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfumeService perfumeService;

    @MockBean
    private PerfumeModelAssembler assembler;

    @Test
    public void testGetPerfumesHATEOAS() throws Exception {
        Perfume perfume = createTestPerfume();
        List<Perfume> perfumes = Arrays.asList(perfume);
        when(perfumeService.getPerfumes()).thenReturn(perfumes);
        when(assembler.toModel(perfume)).thenReturn(
                EntityModel.of(perfume,
                        linkTo(methodOn(PerfumeControllerV2.class).getPerfumeById(1L)).withSelfRel(),
                        linkTo(methodOn(PerfumeControllerV2.class).getPerfumes()).withRel("all-perfumes")
                )
        );

        mockMvc.perform(get("/api/v2/perfumes/listAll").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.perfumeList[0].name").value("Spirited Away"))
                .andExpect(jsonPath("$._embedded.perfumeList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.perfumeList[0]._links.all-perfumes.href").exists());
    }

    @Test
    public void testGetIdHateOasTrue() throws Exception {
        Perfume perfume = createTestPerfume();
        when(perfumeService.getPerfumeByIdOpt(1L)).thenReturn(Optional.of(perfume));
        when(assembler.toModel(perfume)).thenReturn(
                EntityModel.of(perfume,
                        linkTo(methodOn(PerfumeControllerV2.class).getPerfumeById(1L)).withSelfRel(),
                        linkTo(methodOn(PerfumeControllerV2.class).getPerfumes()).withRel("all-perfumes")
                )
        );

        mockMvc.perform(get("/api/v2/perfumes/search/1").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Spirited Away"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.all-perfumes.href").exists());
    }

    @Test
    public void testGetIdHateOasFalse() throws Exception {
        when(perfumeService.getPerfumeByIdOpt(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/perfumes/search/1").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Perfume with ID : 1 not found"));
    }


    private Perfume createTestPerfume() {
        return Perfume.builder()
                .id(1L)
                .name("Spirited Away")
                .stock(5)
                .price(2001)
                .brand("Ghibli")
                .build();
    }



}
