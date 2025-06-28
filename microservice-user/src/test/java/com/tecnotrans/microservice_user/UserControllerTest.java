package com.tecnotrans.microservice_user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tecnotrans.microservice_user.Controller.UserController;
import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Service.UserService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    public void testGetUsers() throws Exception{
        when(userService.getUsers()).thenReturn(Arrays.asList(createTestUser()));
        
        this.mockMvc.perform(get("/api/v1/users/listAll")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    @Test
    public void testGetById() throws Exception{
        when(userService.getUserById(1l)).thenReturn(createTestUser());
        
        this.mockMvc.perform(get("/api/v1/users/search/1")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    private User createTestUser() {
        return User.builder()
        .userId(1l)
        .email("example@mail.com")
        .phoneNumber("+56912345678")
        .name("Juan").build();
    }
}
