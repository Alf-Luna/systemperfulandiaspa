package com.tecnotrans.microservice_user.controller;

import com.tecnotrans.microservice_user.model.User;
import com.tecnotrans.microservice_user.service.UserServiceImpl;
import com.tecnotrans.microservice_user.dto.UserDTO;
import com.tecnotrans.microservice_user.assemblers.UserModelAssembler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1/users")
public class UserControllerV2 {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserModelAssembler assembler;

    /**
     * Obtiene todos los usuarios registrados.
     * @return Lista de usuarios en formato HATEOAS.
     */
    @GetMapping
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<EntityModel<User>> users = userService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(users);
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario.
     * @return Usuario encontrado o mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findByIdOpt(id);
        return user.map(assembler::toModel)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo usuario.
     * @param userDTO Datos del usuario.
     * @return Usuario creado o error de conflicto si ya existe.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@RequestBody UserDTO userDTO){
        try{
            User user = new User();
            user.setId(userDTO.getId());
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            // Agrega aquí otros campos necesarios

            User userSaved = userService.save(user);

            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userSaved.getId())
                .toUri();

            return ResponseEntity.created(location).body(assembler.toModel(userSaved));
        }
        catch(DataIntegrityViolationException e){
            Map<String,String> error = new HashMap<>();
            error.put("message","El email ya está registrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    /**
     * Actualiza un usuario existente.
     * @param id ID del usuario a actualizar.
     * @param userDTO Datos nuevos del usuario.
     * @return Usuario actualizado o error si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        try{
            User user = new User();
            user.setId(id);
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            // Agrega aquí otros campos necesarios

            User userUpdated = userService.save(user);

            return ResponseEntity.ok(assembler.toModel(userUpdated));
        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar.
     * @return Respuesta sin contenido si se elimina correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        userService.deleteById(id);
        return