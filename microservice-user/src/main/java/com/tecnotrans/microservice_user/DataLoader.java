package com.tecnotrans.microservice_user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Repository.UserRepository;

import net.datafaker.Faker;

@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 11; i++){
            User user = new User();
            user.setEmail(faker.name().firstName() + "." + faker.name().lastName() + "@mail.com");
            user.setName(faker.name().fullName());
            user.setPhoneNumber(faker.phoneNumber().phoneNumberInternational());
            userRepository.save(user);
        }

    }

}
