package com.tecnotrans.microservice_user.Assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tecnotrans.microservice_user.Model.User;

import com.tecnotrans.microservice_user.Controller.UserControllerV2;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<User> toModel(User user) {

        return EntityModel.of(user,
            linkTo(methodOn(UserControllerV2.class).getById(user.getUserId())).withSelfRel(),
            linkTo(methodOn(UserControllerV2.class).getUsers()).withRel("all-users")
        );
    }
}


