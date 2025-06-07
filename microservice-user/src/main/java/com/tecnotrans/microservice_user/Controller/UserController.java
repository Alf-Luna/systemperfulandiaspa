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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tecnotrans.microservice_user.Model.User;
import com.tecnotrans.microservice_user.Service.UserService;
import com.tecnotrans.microservice_user.dto.UserDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/listAll")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/search/{id}")    
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
    public ResponseEntity<?> save(@Valid @RequestBody UserDTO userDTO){
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

    @DeleteMapping("/id")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/validateuser/{id}")
    public boolean validateUser(@PathVariable long id){
        return userService.validateUser(id);
    }
}
