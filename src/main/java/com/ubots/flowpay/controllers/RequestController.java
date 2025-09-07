package com.ubots.flowpay.controllers;

import com.ubots.flowpay.*;
import com.ubots.flowpay.exceptions.RequestNotFoundException;
import com.ubots.flowpay.repositories.RequestRepository;
import com.ubots.flowpay.services.AttendantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RequestController {

    RequestRepository requestRepository;
    AttendantService attendantService;

    RequestController(RequestRepository requestRepository, AttendantService attendantService) {
        this.requestRepository = requestRepository;
        this.attendantService = attendantService;
    }

    @GetMapping("/request")
    public List<Request> getRequests() {
        return requestRepository.findAll();
    }

    @GetMapping("/request/{id}")
    public Request getRequest(@PathVariable Long id){
        return requestRepository.findById(id).orElse(null);
    }

    @PostMapping("/request/{type}")
    public Request postRequest(@PathVariable("type") int type)
    {
        // TODO: Exception se type for numero errado
        Team team = Team.fromInt(type);

        Attendant attendant = attendantService.getAttendantWithLessRequests(team);

        //TODO: fazer de fato a request em um Service
        Request request = new Request(RequestStatus.ON_HOLD, team);
        request.setAttendant(attendant);
        attendant.incrementRequestCount();

        return requestRepository.save(request);
    }

    @PostMapping("/request/{id}/complete")
    public Request completeRequest(@PathVariable Long id)
    {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));

        request.setStatus(RequestStatus.COMPLETED);

        return requestRepository.save(request);
    }

    @PostMapping("/request/{id}/cancel")
    public Request cancelRequest(@PathVariable Long id)
    {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));

        request.setStatus(RequestStatus.CANCELED);
        attendantService.DecrementAttendantRequestCount(request.getAttendant());

        return requestRepository.save(request);
    }
}
