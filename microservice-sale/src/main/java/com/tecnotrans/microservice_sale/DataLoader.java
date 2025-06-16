package com.tecnotrans.microservice_sale;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tecnotrans.microservice_sale.Model.Sale;
import com.tecnotrans.microservice_sale.Repository.ISaleRepository;

@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    private ISaleRepository saleRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();

        for (int i = 0; i < 5; i++){
            Sale sale = new Sale();
            //makes a localdatetime, each value is random
            sale.setDate(java.time.LocalDateTime.of(
                random.nextInt(2020, 2025), //YEAR
                random.nextInt(1,13), //MONTH
                random.nextInt(1,29), //DAY
                random.nextInt(1, 24), //HOUR
                random.nextInt(0, 60), //MINUTE
                random.nextInt(0, 60))); //SECOND
            sale.setIdPerfume(random.nextLong(1, 11));
            sale.setIdUser(random.nextLong(1, 11));
            sale.setQty(random.nextInt(1, 9));
            
            saleRepository.save(sale);
        }
    }

}
