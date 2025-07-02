package com.tecnotrans.microservice_user.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.tecnotrans.microservice_user.Assembler.UserModelAssembler;
import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v2/users")
@Tag(name = "Users V2", description = "API for managing users with HATEOAS support")
public class UserControllerV2 {

    @Autowired
    private UserService userService;

    @Autowired
    private UserModelAssembler assembler;


    @GetMapping(value = "/listAll", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Get all users", 
        description = "Returns a list of all users registered on the system with HATEOAS links")
        @ApiResponses(value = {
            @ApiResponse(
                         responseCode = "200",
                         description = "User found",
                         content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = User.class),
                                examples = @ExampleObject(value = "{\"userId\":1,\"name\":\"Juan Lopez\",\"phoneNumber\":\"98765432\",\"email\":\"email@example.com\",\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/v1/users/1\"},\"all-users\":{\"href\":\"http://localhost:8090/api/v1/users/listAll\"}}}"))),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\"}")))    
                            })
    public CollectionModel<EntityModel<User>> getUsers() {
        List<EntityModel<User>> users = userService.getUsers().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,
                    linkTo(methodOn(UserControllerV2.class).getUsers()).withSelfRel());
    }

    @GetMapping(value = "/search/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Get 1 user by ID", 
        description = "Returns a user using the ID number with HATEOAS links")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "User found",
                         content = @Content(mediaType = "application/hal.json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "{\"userId\":1,\"name\":\"Juan Lopez\",\"phoneNumber\":\"98765432\",\"email\":\"email@example.com\",\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/v2/users/1\"},\"all-users\":{\"href\":\"http://localhost:8090/api/v2/users/listAll\"}}}"))),
            @ApiResponse(responseCode = "404", 
                         description = "User not found",
                         content = @Content(
                            mediaType = "application/hal.json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"error\": \"User with ID: X not found\"}")))})
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<User> user = userService.getUserByIdOpt(id);

    if (user.isPresent()) {
        EntityModel<User> model = assembler.toModel(user.get());
        return ResponseEntity.ok()
                .header("mi-encabezado", "valor")
                .body(model);
    } else {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("message", "User with ID : " + id + " not found");
        errorBody.put("status", "404");
        errorBody.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }
        }
}
