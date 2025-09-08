package com.ubots.flowpay.controllers;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.services.AttendantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AttendantController {
    AttendantService attendantService;

    AttendantController(AttendantService attendantService) {
        this.attendantService = attendantService;
    }

    @GetMapping("/attendants")
    public List<Attendant> getAttendants() {
        return attendantService.getAllAttendants();
    }

    @GetMapping("attendants/team/{team}")
    public List<Attendant> getAttendantsByTeam(@PathVariable("team") int team) {
        return attendantService.getAttendantsByTeam(team);
    }

    @GetMapping("/attendants/{id}")
    public Attendant getAttendantById(@PathVariable Integer id) {
        return attendantService.getAttendantById(id);
    }

    @PostMapping("/attendants")
    public Attendant postAttendant(@RequestBody Attendant attendant) {
        return attendantService.createAttendant(attendant);
    }

}
