package com.tecnotrans.microservice_user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.tecnotrans.microservice_user.Controller.UserController;
import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Service.UserService;

import net.minidev.json.JSONObject;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    public void testGetUsers() throws Exception{
        when(userService.getUsers()).thenReturn(Arrays.asList(createTestUser()));
        
        mockMvc.perform(get("/api/v1/users/listAll")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    @Test
    public void testGetByIdTrue() throws Exception{
        when(userService.getUserByIdOpt(1l)).thenReturn(Optional.of(createTestUser()));
        
        mockMvc.perform(get("/api/v1/users/search/1")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value("Juan"));
    }

    @Test
    public void testGetByIdFalse() throws Exception{
        when(userService.getUserByIdOpt(1l)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/v1/users/search/1")).andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testSave() throws Exception{
        User user = createTestUser();

        when(userService.addUser(user)).thenReturn(user);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("userId", "1");
        jsonUser.put("name", "Juan");
        jsonUser.put("phoneNumber", "+56912345678");
        jsonUser.put("email", "example@mail.com");

        mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("name").value("Juan"));
    }

    @Test
    public void testSaveConflict() throws Exception{
        User user = createTestUser();

        when(userService.addUser(user)).thenThrow(new DataIntegrityViolationException("Dato repetido"));

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("userId", "1");
        jsonUser.put("name", "Juan");
        jsonUser.put("phoneNumber", "+56912345678");
        jsonUser.put("email", "example@mail.com");

        mockMvc.perform(post("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isConflict());
    }

    @Test
    public void testUpdate() throws Exception{
        User user = createTestUser();

        when(userService.addUser(user)).thenReturn(user);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("userId", "1");
        jsonUser.put("name", "Juan");
        jsonUser.put("phoneNumber", "+56912345678");
        jsonUser.put("email", "example@mail.com");

        mockMvc.perform(put("/api/v1/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value("Juan"));
    }

    @Test
    public void testUpdateNotFound() throws Exception{
        User user = createTestUser();

        when(userService.addUser(user)).thenReturn(user);

        JSONObject jsonUser = new JSONObject();

        mockMvc.perform(put("/api/v1/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonUser.toString()))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception{
        when(userService.deleteUserById(1l)).thenReturn("");

        mockMvc.perform(delete("/api/v1/users/1"))
        .andDo(print())
        .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNotFound() throws Exception{
        when(userService.deleteUserById(1l)).thenThrow(new IllegalArgumentException("Id invalida"));

        mockMvc.perform(delete("/api/v1/users/1"))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testValidateUser() throws Exception{
        when(userService.validateUser(1l)).thenReturn(true);

        mockMvc.perform(get("/api/v1/users/validateuser/1"))
        .andDo(print())
        .andExpect(status().isOk());
    }

    private User createTestUser() {
        return User.builder()
        .userId(1l)
        .email("example@mail.com")
        .phoneNumber("+56912345678")
        .name("Juan").build();
    }
}
