package com.tecnotrans.microservice_sale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Repository.ISaleRepository;

@ExtendWith(MockitoExtension.class)
public class TestSaleDataLoader {

    @Mock
    ISaleRepository saleRepository;

    @InjectMocks
    DataLoader dataLoader;

    @Test
    public void testDataLoaderRun() throws Exception{
        dataLoader.run();

        verify(saleRepository, atLeastOnce()).save(any(Sale.class));
    }
}
