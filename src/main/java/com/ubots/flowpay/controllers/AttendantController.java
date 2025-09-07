package com.ubots.flowpay.controllers;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.exceptions.AttendantNotFoundException;
import com.ubots.flowpay.repositories.AttendantRepository;
import com.ubots.flowpay.Team;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AttendantController {

    AttendantRepository attendantRepository;

    AttendantController(AttendantRepository attendantRepository) {
        this.attendantRepository = attendantRepository;
    }

    @GetMapping("/attendants")
    public List<Attendant> getAttendants() {
        return attendantRepository.findAll();
    }

    @GetMapping("attendants/team/{team}")
    public List<Attendant> getAttendantsByTeam(@PathVariable("team") int team) {
        return attendantRepository.findAttendantByTeam(Team.fromInt(team));
    }

    @GetMapping("/attendants/{id}")
    public Attendant getAttendantById(@PathVariable Integer id) {
        return attendantRepository.findById(id).orElseThrow(() -> new AttendantNotFoundException(id));
    }

    @PostMapping("/attendants")
    public Attendant postAttendant(@RequestBody Attendant attendant) {
        return attendantRepository.save(attendant);
    }

}
