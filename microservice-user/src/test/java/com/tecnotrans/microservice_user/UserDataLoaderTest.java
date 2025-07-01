package com.tecnotrans.microservice_user;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDataLoaderTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    DataLoader dataLoader;

    @Test
    public void testDataLoaderRun() throws Exception{
        dataLoader.run();

        verify(userRepository, atLeastOnce()).save(any(User.class));
    }
}
