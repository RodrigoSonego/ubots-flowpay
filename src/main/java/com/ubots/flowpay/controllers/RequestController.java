package com.ubots.flowpay.controllers;

import com.ubots.flowpay.*;
import com.ubots.flowpay.services.RequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RequestController {

    RequestService requestService;

    RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/request")
    public List<Request> getRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/request/{id}")
    public Request getRequest(@PathVariable Long id){
        return requestService.getRequestById(id);
    }

    @PostMapping("/request/{type}")
    public Request postRequest(@PathVariable("type") int type)
    {
        return requestService.createRequest(type);
    }

    @PostMapping("/request/{id}/complete")
    public Request completeRequest(@PathVariable Long id)
    {
        return requestService.completeRequest(id);
    }

    @PostMapping("/request/{id}/cancel")
    public Request cancelRequest(@PathVariable Long id)
    {
        return requestService.cancelRequest(id);
    }
}
