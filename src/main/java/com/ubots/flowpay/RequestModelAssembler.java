package com.ubots.flowpay;

import com.ubots.flowpay.controllers.AttendantController;
import com.ubots.flowpay.controllers.RequestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RequestModelAssembler implements RepresentationModelAssembler<Request, EntityModel<Request>>
{
    @Override
    public EntityModel<Request> toModel(Request request) {
        EntityModel<Request> model = EntityModel.of(request,
                linkTo(methodOn(RequestController.class).getRequest(request.getId())).withSelfRel(),
                linkTo(methodOn(RequestController.class).getRequests()).withRel("requests"));

        if(request.getStatus() == RequestStatus.IN_PROGRESS){
            model.add(linkTo(methodOn(AttendantController.class).getAttendantById(request.getAttendant().getId())).withRel("attendant"));
            model.add(linkTo(methodOn(RequestController.class).completeRequest(request.getId())).withRel("complete"));
            model.add(linkTo(methodOn(RequestController.class).cancelRequest(request.getId())).withRel("cancel"));
        }

        return model;
    }
}
