package com.tecnotrans.microservice_perfume;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tecnotrans.microservice_perfume.Model.Perfume;
import com.tecnotrans.microservice_perfume.Repository.PerfumeRepository;

import net.datafaker.Faker;

@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    private PerfumeRepository perfumeRepository;
    
    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 10; i++){
            Perfume perfume = new Perfume();
            perfume.setBrand(faker.studioGhibli().character());
            perfume.setName(faker.studioGhibli().movie());
            perfume.setPrice(random.nextInt(10, 300) + 990);
            perfume.setStock(random.nextInt(50));
            perfumeRepository.save(perfume);
        }

    }

}
