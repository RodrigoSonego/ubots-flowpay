package com.ubots.flowpay;

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
        return attendantRepository.findById(id).orElse(null);
    }

    @PostMapping("/attendants")
    public Attendant postAttendant(@RequestBody Attendant attendant) {
        return attendantRepository.save(attendant);
    }

}
