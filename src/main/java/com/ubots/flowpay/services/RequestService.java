package com.ubots.flowpay.services;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.Request;
import com.ubots.flowpay.RequestStatus;
import com.ubots.flowpay.Team;
import com.ubots.flowpay.controllers.RequestController;
import com.ubots.flowpay.exceptions.RequestNotFoundException;
import com.ubots.flowpay.repositories.AttendantRepository;
import com.ubots.flowpay.repositories.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.SynchronousQueue;

@Service
public class RequestService {

    private RequestRepository requestRepository;
    private AttendantService attendantService;

    SynchronousQueue<Request> queue;

    RequestService(RequestRepository requestRepository, AttendantService attendantService) {
        this.requestRepository = requestRepository;
        this.attendantService = attendantService;

        queue = new SynchronousQueue<>();
    }

    public Request createRequest(int type)
    {
        // TODO: Exception se type for numero errado
        Team team = Team.fromInt(type);

        // TODO: tratar se é null
        Attendant attendant = attendantService.getAttendantWithLessRequests(team);

        Logger log = LoggerFactory.getLogger(RequestController.class);
        log.info(String.format("attendant got for request: %S", attendant == null ? "null" : attendant.getName()));

        if  (attendant == null){
            Request request = new Request(RequestStatus.ON_HOLD, team);
            return requestRepository.save(request);
        }

        //TODO: fazer de fato a request em um Service
        Request request = new Request(RequestStatus.IN_PROGRESS, team);
        request.setAttendant(attendant);
        attendantService.onRequestAssignedToAttendant(attendant);

        return requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(Long id){
        return requestRepository.findById(id).orElse(null);
    }

    public Request completeRequest(Long id)
    {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));

        request.setStatus(RequestStatus.COMPLETED);

        return requestRepository.save(request);
    }

    public Request cancelRequest(Long id){
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));

        request.setStatus(RequestStatus.CANCELED);
        attendantService.DecrementAttendantRequestCount(request.getAttendant());

        return requestRepository.save(request);
    }

    private void addRequestToQueue(Request request) {
        queue.offer(request);
    }

    public Request getRequestFromQueue(){
        return queue.poll();
    }

}
