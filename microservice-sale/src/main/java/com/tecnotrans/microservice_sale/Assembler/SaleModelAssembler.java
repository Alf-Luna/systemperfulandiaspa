package com.tecnotrans.microservice_sale.Assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tecnotrans.microservice_sale.Model.Sale;

import com.tecnotrans.microservice_sale.Controller.SaleControllerV2;

@Component
public class SaleModelAssembler implements RepresentationModelAssembler<Sale, EntityModel<Sale>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Sale> toModel(Sale sale) {

        return EntityModel.of(sale,
            linkTo(methodOn(SaleControllerV2.class).findByID(sale.getId())).withSelfRel(),
            linkTo(methodOn(SaleControllerV2.class).findAll()).withRel("all-perfumes")
        );
    }
}
