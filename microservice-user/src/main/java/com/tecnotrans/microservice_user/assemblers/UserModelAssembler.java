/*
package com.tecnotrans.microservice_user.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tecnotrans.microservice_user.model.Sale;
import com.tecnotrans.microservice_user.controller.SaleControllerV2;

@Component
public class SaleModelAssembler implements RepresentationModelAssembler<Sale, EntityModel<Sale>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Sale> toModel(Sale sale) {

        return EntityModel.of(sale,
            linkTo(methodOn(SaleControllerV2.class).getSaleById(sale.getId())).withSelfRel(),
            linkTo(methodOn(SaleControllerV2.class).getAllSales()).withRel("all-sales")
        );
    }
}

*/

package com.tecnotrans.microservice_user.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tecnotrans.microservice_user.model.User;
import com.tecnotrans.microservice_user.controller.UserControllerV2;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
            linkTo(methodOn(UserControllerV2.class).getUserById(user.getId())).withSelfRel(),
            linkTo(methodOn(UserControllerV2.class).getAllUsers()).withRel("all-users")
        );
    }
}