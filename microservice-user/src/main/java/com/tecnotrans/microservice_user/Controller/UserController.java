package com.tecnotrans.microservice_user.Controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Service.UserService;
import com.tecnotrans.microservice_user.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "Users", description = "API for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/listAll")
    @Operation(
        summary = "Get all users", 
        description = "Returns a list of all users registered on the system",
        responses = {
            @ApiResponse(responseCode = "200", 
                         description = "Succesful retrieval of users list",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", 
                         description = "Internal server error",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\"}")
                         ))
        }
            )
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/search/{id}")
    @Operation(
        summary = "Get 1 user by ID", 
        description = "Returns a user using the ID number",
        parameters = {@Parameter(name = "id", description = "ID of the user to retrieve", required = true, example = "1")})
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "User found",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", 
                         description = "User not found",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"error\": \"No se encontró el usuario con esa ID: X\"}")))
                        } )
    public ResponseEntity<?> getById(@PathVariable Long id){
        Optional<User> user = userService.getUserByIdOpt(id);    
        
        if(user.isPresent()){
            return ResponseEntity.ok()
                        .header("mi-encabezado","valor")
                        .body(user.get());
        }
        else{
            //Respuesta de error con cuerpo personalizado
            Map<String,String> errorBody = new HashMap<>();
            errorBody.put("message","No se encontró el usuario con esa ID: " + id);
            errorBody.put("status","404");
            errorBody.put("timestamp",LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorBody);
        }
    }

    @PostMapping
    @Operation(
        summary = "Creates a new user", 
        description = "Creates and saves a user using the provided details",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User data to create",
                required = true,
                content = @Content(schema = @Schema(implementation = UserDTO.class))
        ))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", 
                         description = "User successfully created",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 500, \"error\": \"Internal Server Error\"}")
                         ))

        }) 
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userDTO){
        try{  
            User user = new User();
            user.setUserId(userDTO.getUserId());
            user.setName(userDTO.getName());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setEmail(userDTO.getEmail());

            User userSaved = userService.addUser(user);

            UserDTO dto = new UserDTO();
            dto.setUserId(userSaved.getUserId());
            dto.setName(userSaved.getName());
            dto.setPhoneNumber(userSaved.getPhoneNumber());
            dto.setEmail(userSaved.getEmail());

            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getUserId())
                .toUri();

            return ResponseEntity.created(location).body(dto);
        } catch(DataIntegrityViolationException e){
            //Ejemplo: Error si hay un campo único duplicado (ej: email repetido)
            Map<String,String> error = new HashMap<>();
            error.put("message","El correo ingresado ya esta en uso");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Updates an existing user", 
        description = "Updates a user using the provided details",
        parameters = {@Parameter(name = "id", description = "ID of the user to update", required = true, example = "1")},
        requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User data to update",
            required = true,
            content = @Content(schema = @Schema(implementation = UserDTO.class))))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                         description = "User successfully updated",
                         content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", 
                         description = "User not found",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"error\": \"No se encontró el usuario con esa ID: X\"}")))
        })
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        try{
            User user = new User();
            user.setUserId(userDTO.getUserId());
            user.setName(userDTO.getName());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setEmail(userDTO.getEmail());

            User userUpdated = userService.addUser(user);

            UserDTO dto = new UserDTO();
            dto.setUserId(userUpdated.getUserId());
            dto.setName(userUpdated.getName());
            dto.setPhoneNumber(userUpdated.getPhoneNumber());
            dto.setEmail(userUpdated.getEmail());

            return ResponseEntity.ok(dto);

        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletes an existing user", 
        description = "Deletes an existing user using the ID number",
        parameters = {@Parameter(name = "id", description = "ID of the user to delete", required = true, example = "1")})
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                         description = "User successfully deleted",
                         content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", 
                         description = "User not found",
                         content = @Content(mediaType = "application/json",
                         examples = @ExampleObject(value = "{\"timestamp\": \"2025-07-01T12:00:00\", \"status\": 404, \"error\": \"No se encontró el usuario con esa ID: X\"}")))
        })
    public ResponseEntity<?> delete(@PathVariable Long id){
        System.out.println("received delete request");
        try{
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/validateuser/{id}")
    @Operation(
        summary = "Validate if a user exists by its ID",
        description = "Checks if a user exists to make a sale using their ID",
        parameters = {@Parameter(name = "id", description = "ID of the user to validate", required = true, example = "5")})
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User exists"),
            @ApiResponse(responseCode = "200", 
                         description = "User not found",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "false")
                         ))
})
    public boolean validateUser(@PathVariable long id){
        return userService.validateUser(id);
    }
}
