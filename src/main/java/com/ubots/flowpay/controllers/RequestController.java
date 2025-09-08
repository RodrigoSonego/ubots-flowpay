package com.ubots.flowpay.controllers;

import com.ubots.flowpay.*;
import com.ubots.flowpay.exceptions.InvalidRequestStatusException;
import com.ubots.flowpay.exceptions.InvalidTeamOrdinalException;
import com.ubots.flowpay.exceptions.RequestNotFoundException;
import com.ubots.flowpay.services.RequestService;
import org.apache.coyote.Response;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getRequest(@PathVariable Long id){
        try{
            Request request = requestService.getRequestById(id);

            return ResponseEntity.ok(assembler.toModel(request));
        }
        catch(RequestNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                                    .withTitle("Not Found")
                                    .withDetail(e.getMessage())
                            );
        }

    }

    @PostMapping("/request/{type}")
    public ResponseEntity<?> postRequest(@PathVariable("type") int type) {
        try {
            Request createdRequest = requestService.createRequest(type);

            return ResponseEntity.ok(assembler.toModel(createdRequest));
        }
        catch (InvalidTeamOrdinalException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Not Found")
                            .withDetail(e.getMessage())
                    );
        }

    }

    @PostMapping("/request/{id}/complete")
    public ResponseEntity<?> completeRequest(@PathVariable Long id) {
        try {
            Request request = requestService.completeRequest(id);

            return ResponseEntity.ok(assembler.toModel(request));

        }
        catch (RequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Not Found")
                            .withDetail(e.getMessage())
                    );
        }
        catch (InvalidRequestStatusException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Method Not Allowed")
                            .withDetail(e.getMessage())
                    );
        }
    }

    @PostMapping("/request/{id}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable Long id) {
        try {
            Request request = requestService.cancelRequest(id);

            return ResponseEntity.ok(assembler.toModel(request));
        }
        catch (RequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Not Found")
                            .withDetail(e.getMessage())
                    );
        }
        catch (InvalidRequestStatusException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Method Not Allowed")
                            .withDetail(e.getMessage())
                    );
        }
    }
}
