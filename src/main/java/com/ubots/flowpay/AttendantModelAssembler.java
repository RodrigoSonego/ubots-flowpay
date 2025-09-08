package com.ubots.flowpay;

import com.ubots.flowpay.controllers.AttendantController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AttendantModelAssembler implements RepresentationModelAssembler<Attendant, EntityModel<Attendant>> {
    @Override
    public EntityModel<Attendant> toModel(Attendant attendant) {
        return EntityModel.of(attendant,
                linkTo(methodOn(AttendantController.class).getAttendantById(attendant.getId())).withSelfRel(),
                linkTo(methodOn(AttendantController.class).getAttendantsByTeam(Team.toInt(attendant.getTeam()))).withRel("team"),
                linkTo(methodOn(AttendantController.class).getAttendants()).withRel("attendants"));
    }
}
