package com.tecnotrans.microservice_user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Repository.UserRepository;
import com.tecnotrans.microservice_user.Service.UserService;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;

    @Test
    public void FindByCode(){
        Long code = 1l;
        User user = User.builder()
        .userId(code)
        .email("example@mail.com")
        .phoneNumber("+5691234567")
        .name("Pedro Pablo").build();

        when(userRepository.findById(code)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(code);

        assertNotNull(foundUser);
        assertEquals(code, foundUser.getUserId());
    }

}
