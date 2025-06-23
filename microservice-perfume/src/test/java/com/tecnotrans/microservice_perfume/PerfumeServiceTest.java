package com.tecnotrans.microservice_perfume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Repository.PerfumeRepository;
import com.tecnotrans.microservice_perfume.Service.PerfumeService;

@ExtendWith(MockitoExtension.class)
public class PerfumeServiceTest {

    @InjectMocks
    private PerfumeService perfumeService;

    @Mock
    private PerfumeRepository perfumeRepository;

    @Test
    public void findAll(){
        when(perfumeRepository.findAll()).thenReturn(List.of(createTestPerfume()));

        List<Perfume> perfumes = perfumeService.getPerfumes();
        
        assertNotNull(perfumes);
        assertEquals(1, perfumes.size());
    }

    @Test
    public void findByCode(){
        Long code = 1l;
        Perfume perfume = createTestPerfume();

        when(perfumeRepository.findById(code)).thenReturn(Optional.of(perfume));

        Perfume foundPerfume = perfumeService.getPerfumeById(code);

        assertNotNull(foundPerfume);
        assertEquals(code, foundPerfume.getId());
    }

    @Test
    public void save(){
        Perfume perfume = createTestPerfume();

        when(perfumeRepository.save(perfume)).thenReturn(perfume);

        Perfume savedPerfume = perfumeService.addPerfume(perfume);

        assertNotNull(savedPerfume);
        assertEquals("Imperial Majesty", savedPerfume.getName());
    }

    @Test
    public void deleteById(){
        Long codigo = 1l;

        doNothing().when(perfumeRepository).deleteById(codigo);

        perfumeService.deletePerfumeById(codigo);

        verify(perfumeRepository, times(1)).deleteById(codigo);
    }

    private Perfume createTestPerfume() {
        return Perfume.builder()
        .id(1l)
        .name("Imperial Majesty")
        .stock(10)
        .price(50.5F)
        .brand("Clive Christian").build();
    }
}
