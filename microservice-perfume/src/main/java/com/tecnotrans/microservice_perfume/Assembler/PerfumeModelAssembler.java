package com.tecnotrans.microservice_perfume.Assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tecnotrans.microservice_perfume.Model.Perfume;

import com.tecnotrans.microservice_perfume.Controller.PerfumeControllerV2;

@Component
public class PerfumeModelAssembler implements RepresentationModelAssembler<Perfume, EntityModel<Perfume>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Perfume> toModel(Perfume perfume) {

        return EntityModel.of(perfume,
            linkTo(methodOn(PerfumeControllerV2.class).getPerfumeById(perfume.getId())).withSelfRel(),
            linkTo(methodOn(PerfumeControllerV2.class).getPerfumes()).withRel("all-perfumes")
        );
    }
}
