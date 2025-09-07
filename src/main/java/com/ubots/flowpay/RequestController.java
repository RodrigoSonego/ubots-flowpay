package com.ubots.flowpay;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RequestController {

    RequestRepository requestRepository;

    RequestController(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
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

        //TODO: fazer de fato a request
        Request request = new Request();
        Attendant attendant = new Attendant("Cleber", team);
        request.setStatus(RequestStatus.ON_HOLD);
        request.setTeam(team);

        return request;
    }
}
