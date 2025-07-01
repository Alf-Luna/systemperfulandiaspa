package com.tecnotrans.microservice_perfume;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.tecnotrans.microservice_perfume.Controller.PerfumeController;
import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Service.PerfumeService;

import net.minidev.json.JSONObject;

@WebMvcTest(PerfumeController.class)
public class PerfumeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PerfumeService perfumeService;

    @Test
    public void testGetUsers() throws Exception{
        when(perfumeService.getPerfumes()).thenReturn(Arrays.asList(createTestPerfume()));
        
        mockMvc.perform(get("/api/v1/perfumes/listAll")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Spirited Away"));
    }

    @Test
    public void testGetByIdTrue() throws Exception{
        when(perfumeService.getPerfumeByIdOpt(1l)).thenReturn(Optional.of(createTestPerfume()));
        
        mockMvc.perform(get("/api/v1/perfumes/search/1")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value("Spirited Away"));
    }

    @Test
    public void testGetByIdFalse() throws Exception{
        when(perfumeService.getPerfumeByIdOpt(1l)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/v1/perfumes/search/1")).andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testSave() throws Exception{
        Perfume perfume = createTestPerfume();

        when(perfumeService.addPerfume(perfume)).thenReturn(perfume);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", perfume.getId());
        jsonUser.put("name", perfume.getName());
        jsonUser.put("stock", perfume.getStock());
        jsonUser.put("price", perfume.getPrice());
        jsonUser.put("brand", perfume.getBrand());

        mockMvc.perform(post("/api/v1/perfumes/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("name").value("Spirited Away"));
    }

    @Test
    public void testSaveFail() throws Exception{
        Perfume perfume = createTestPerfume();

        when(perfumeService.addPerfume(perfume)).thenThrow(new DataIntegrityViolationException("Invalid Data"));

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", perfume.getId());
        jsonUser.put("name", perfume.getName());
        jsonUser.put("stock", perfume.getStock());
        jsonUser.put("price", perfume.getPrice());
        jsonUser.put("brand", perfume.getBrand());

        mockMvc.perform(post("/api/v1/perfumes/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isConflict());
    }

    @Test
    public void testUpdate() throws Exception{
        Perfume perfume = createTestPerfume();

        when(perfumeService.addPerfume(perfume)).thenReturn(perfume);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", perfume.getId());
        jsonUser.put("name", perfume.getName());
        jsonUser.put("stock", perfume.getStock());
        jsonUser.put("price", perfume.getPrice());
        jsonUser.put("brand", perfume.getBrand());

        mockMvc.perform(put("/api/v1/perfumes/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value("Spirited Away"));
    }

    @Test
    public void testUpdateNotFound() throws Exception{
        Perfume perfume = createTestPerfume();

        when(perfumeService.addPerfume(perfume)).thenReturn(perfume);

        JSONObject jsonUser = new JSONObject();

        mockMvc.perform(put("/api/v1/perfumes/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception{
        when(perfumeService.deletePerfumeById(1l)).thenReturn("");

        mockMvc.perform(delete("/api/v1/perfumes/1"))
        .andDo(print())
        .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNotFound() throws Exception{
        when(perfumeService.deletePerfumeById(1l)).thenThrow(new IllegalArgumentException("Id invalida"));

        mockMvc.perform(delete("/api/v1/perfumes/1"))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testDarPerfume() throws Exception{
        Perfume perfume = createTestPerfume();

        when(perfumeService.getPerfumeById(1L)).thenReturn(perfume);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", perfume.getId());
        jsonUser.put("name", perfume.getName());
        jsonUser.put("stock", perfume.getStock());
        jsonUser.put("price", perfume.getPrice());
        jsonUser.put("brand", perfume.getBrand());

        mockMvc.perform(get("/api/v1/perfumes/darPerfume/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value("Spirited Away"));
    }

    @Test
    public void testAdjustStock() throws Exception {
        Perfume perfume = createTestPerfume();

        when(perfumeService.getPerfumeById(1l)).thenReturn(perfume);
        when(perfumeService.addPerfume(perfume)).thenReturn(perfume);

        mockMvc.perform(post("/api/v1/perfumes/adjustStock/1")
            .param("substract", "3"));

        verify(perfumeService, times(1)).addPerfume(any(Perfume.class));

    }

    private Perfume createTestPerfume() {
        return Perfume.builder()
        .id(1l)
        .name("Spirited Away")
        .stock(5)
        .price(2001)
        .brand("Ghibli").build();
    }
}
