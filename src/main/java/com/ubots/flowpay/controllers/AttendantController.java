package com.ubots.flowpay.controllers;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.AttendantModelAssembler;
import com.ubots.flowpay.exceptions.AttendantNotFoundException;
import com.ubots.flowpay.exceptions.InvalidAttendantException;
import com.ubots.flowpay.exceptions.InvalidTeamOrdinalException;
import com.ubots.flowpay.services.AttendantService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AttendantController {
    AttendantService attendantService;
    AttendantModelAssembler  attendantModelAssembler;

    AttendantController(AttendantService attendantService, AttendantModelAssembler attendantModelAssembler) {
        this.attendantService = attendantService;
        this.attendantModelAssembler = attendantModelAssembler;
    }

    @GetMapping("/attendants")
    public CollectionModel<EntityModel<Attendant>> getAttendants() {
        List<Attendant> attendants = attendantService.getAllAttendants();

        List<EntityModel<Attendant>> entities =  attendants.stream()
                .map(attendantModelAssembler::toModel).toList();

        return CollectionModel.of(entities, linkTo(methodOn(AttendantController.class).getAttendants()).withSelfRel());
    }

    @GetMapping("attendants/team/{team}")
    public ResponseEntity<?> getAttendantsByTeam(@PathVariable("team") int team) {
        try {
            List<Attendant> attendants = attendantService.getAttendantsByTeam(team);
            List<EntityModel<Attendant>> entities = attendants.stream()
                    .map(attendantModelAssembler::toModel).toList();

            CollectionModel<EntityModel<Attendant>> attendantModels = CollectionModel.of(entities, linkTo(methodOn(AttendantController.class).getAttendantsByTeam(team)).withSelfRel());

            return ResponseEntity.ok(attendantModels);
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

    @GetMapping("/attendants/{id}")
    public ResponseEntity<?> getAttendantById(@PathVariable Integer id) {

        try {
            Attendant attendant = attendantService.getAttendantById(id);

            return ResponseEntity.ok(attendantModelAssembler.toModel(attendant));

        }
        catch (AttendantNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Not Found")
                            .withDetail(e.getMessage())
                    );
        }

    }

    @PostMapping("/attendants")
    public ResponseEntity<?> postAttendant(@RequestBody Attendant attendant) {
        try {

            Attendant createdAttendant = attendantService.createAttendant(attendant);

            return ResponseEntity.ok(attendantModelAssembler.toModel(createdAttendant));

        }
        catch (InvalidAttendantException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Invalid Object")
                            .withDetail(e.getMessage())
                    );

        }

    }

}
