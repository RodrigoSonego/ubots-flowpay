package com.ubots.flowpay.controllers;

import com.ubots.flowpay.*;
import com.ubots.flowpay.services.RequestService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RequestController {

    RequestService requestService;
    RequestModelAssembler  assembler;


    RequestController(RequestService requestService,  RequestModelAssembler assembler) {
        this.requestService = requestService;
        this.assembler = assembler;
    }

    @GetMapping("/request")
    public CollectionModel<EntityModel<Request>> getRequests() {
        List<Request> requests = requestService.getAllRequests();

        List<EntityModel<Request>> entities = requests.stream().map(assembler::toModel).toList();

        return CollectionModel.of(entities, linkTo(methodOn(RequestController.class).getRequests()).withSelfRel());
    }

    @GetMapping("/request/{id}")
    public EntityModel<Request> getRequest(@PathVariable Long id){
        Request request = requestService.getRequestById(id);

        return assembler.toModel(request);
    }

    @PostMapping("/request/{type}")
    public EntityModel<Request> postRequest(@PathVariable("type") int type)
    {
        Request createdRequest = requestService.createRequest(type);

        return assembler.toModel(createdRequest);
    }

    @PostMapping("/request/{id}/complete")
    public EntityModel<Request> completeRequest(@PathVariable Long id)
    {
        Request request = requestService.completeRequest(id);

        return assembler.toModel(request);
    }

    @PostMapping("/request/{id}/cancel")
    public EntityModel<Request> cancelRequest(@PathVariable Long id)
    {
        Request request = requestService.cancelRequest(id);

        return assembler.toModel(request);
    }
}
