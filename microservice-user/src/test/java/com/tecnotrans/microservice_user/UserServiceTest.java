package com.tecnotrans.microservice_user;

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
    public void findAll(){
        when(userRepository.findAll()).thenReturn(List.of(createTestUser()));

        List<User> users = userService.getUsers();
        
        assertNotNull(users);
        assertEquals(1, users.size());
    }
    
    @Test
    public void findByCode(){
        Long code = 1l;
        User user = createTestUser();

        when(userRepository.findById(code)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(code);

        assertNotNull(foundUser);
        assertEquals(code, foundUser.getUserId());
    }

    @Test
    public void save(){
        User user = createTestUser();

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals("Juan", savedUser.getName());
    }

    @Test
    public void deleteById(){
        Long codigo = 1l;

        doNothing().when(userRepository).deleteById(codigo);

        userService.deleteUserById(codigo);

        verify(userRepository, times(1)).deleteById(codigo);
    }

    private User createTestUser() {
        return User.builder()
        .userId(1l)
        .email("example@mail.com")
        .phoneNumber("+56912345678")
        .name("Juan").build();
    }
}
