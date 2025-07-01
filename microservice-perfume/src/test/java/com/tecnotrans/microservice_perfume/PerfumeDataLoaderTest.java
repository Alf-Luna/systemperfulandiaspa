package com.tecnotrans.microservice_perfume;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Repository.PerfumeRepository;

@ExtendWith(MockitoExtension.class)
public class PerfumeDataLoaderTest {
    
    @Mock
    PerfumeRepository perfumeRepository;

    @InjectMocks
    DataLoader dataLoader;

    @Test
    public void testDataLoaderRun() throws Exception{
        dataLoader.run();

        verify(perfumeRepository, atLeastOnce()).save(any(Perfume.class));
    }
}
