package com.ubots.flowpay.controllers;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.AttendantModelAssembler;
import com.ubots.flowpay.services.AttendantService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.stream.Collectors;

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
    public CollectionModel<EntityModel<Attendant>> getAttendantsByTeam(@PathVariable("team") int team) {
        List<Attendant> attendants = attendantService.getAttendantsByTeam(team);

        List<EntityModel<Attendant>> entities = attendants.stream()
                .map(attendantModelAssembler::toModel).toList();

        return CollectionModel.of(entities, linkTo(methodOn(AttendantController.class).getAttendantsByTeam(team)).withSelfRel());
    }

    @GetMapping("/attendants/{id}")
    public EntityModel<Attendant> getAttendantById(@PathVariable Integer id) {
        Attendant attendant = attendantService.getAttendantById(id);

        return attendantModelAssembler.toModel(attendant);
    }

    @PostMapping("/attendants")
    public EntityModel<Attendant> postAttendant(@RequestBody Attendant attendant) {
        Attendant createdAttendant = attendantService.createAttendant(attendant);

        return attendantModelAssembler.toModel(createdAttendant);
    }

}
