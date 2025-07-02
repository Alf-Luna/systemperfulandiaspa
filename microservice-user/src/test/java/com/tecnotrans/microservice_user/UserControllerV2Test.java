package com.tecnotrans.microservice_user;

import com.tecnotrans.microservice_user.Controller.UserControllerV2;
import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Service.UserService;
import com.tecnotrans.microservice_user.Assembler.UserModelAssembler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@WebMvcTest(UserControllerV2.class)
public class UserControllerV2Test {

@Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserModelAssembler assembler;

    @Test
    public void testGetUsersHATEOAS() throws Exception {
        User user = createTestUser();
        List<User> users = Arrays.asList(user);
        when(userService.getUsers()).thenReturn(users);
        when(assembler.toModel(user)).thenReturn(
                EntityModel.of(user,
                        linkTo(methodOn(UserControllerV2.class).getById(1L)).withSelfRel(),
                        linkTo(methodOn(UserControllerV2.class).getUsers()).withRel("all-users")
                )
        );

         mockMvc.perform(get("/api/v2/users/listAll").accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.userList[0].name").value("Juan"))
            .andExpect(jsonPath("$._embedded.userList[0].email").value("example@mail.com"))
            .andExpect(jsonPath("$._embedded.userList[0]._links.self.href").exists())
            .andExpect(jsonPath("$._embedded.userList[0]._links.all-users.href").exists());
    }

    @Test
    public void testGetIdHateOasTrue() throws Exception {
        User user = createTestUser();
        when(userService.getUserByIdOpt(1L)).thenReturn(Optional.of(user));
        when(assembler.toModel(user)).thenReturn(
                EntityModel.of(user,
                        linkTo(methodOn(UserControllerV2.class).getById(1L)).withSelfRel(),
                        linkTo(methodOn(UserControllerV2.class).getUsers()).withRel("all-users")
                )
        );

        mockMvc.perform(get("/api/v2/users/search/1").accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Juan"))
            .andExpect(jsonPath("$.email").value("example@mail.com"))
            .andExpect(jsonPath("$._links.self.href").exists())
            .andExpect(jsonPath("$._links.all-users.href").exists());
    }

    @Test
    public void testGetIdHateOasFalse() throws Exception {
        when(userService.getUserByIdOpt(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/users/search/1").accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("User with ID : 1 not found"));
    }

    private User createTestUser() {
        return User.builder()
        .userId(1l)
        .email("example@mail.com")
        .phoneNumber("+56912345678")
        .name("Juan").build();
    }

}
