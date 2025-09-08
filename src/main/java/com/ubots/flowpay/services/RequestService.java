package com.ubots.flowpay.services;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.Request;
import com.ubots.flowpay.RequestStatus;
import com.ubots.flowpay.Team;
import com.ubots.flowpay.exceptions.InvalidRequestStatusException;
import com.ubots.flowpay.exceptions.InvalidTeamOrdinalException;
import com.ubots.flowpay.exceptions.RequestNotFoundException;
import com.ubots.flowpay.repositories.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final AttendantService attendantService;

    HashMap<Team, ArrayBlockingQueue<Request>> requestQueues;

    Logger log = LoggerFactory.getLogger(RequestService.class);

    RequestService(RequestRepository requestRepository, @Lazy AttendantService attendantService) {
        this.requestRepository = requestRepository;
        this.attendantService = attendantService;

        requestQueues = new HashMap<>();
    }

    public Request createRequest(int type)
    {
        if (!Team.isValidOrdinal(type)) {
            throw new InvalidTeamOrdinalException(type);
        }

        Team team = Team.fromInt(type);

        Attendant attendant = attendantService.getAttendantWithLessRequests(team);

        if  (attendant == null){
            Request request = new Request(RequestStatus.ON_HOLD, team);

            try {
                addRequestToQueue(request);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return requestRepository.save(request);
        }

        Request request = new Request(RequestStatus.IN_PROGRESS, team);
        request.setAttendant(attendant);
        attendantService.onRequestAssignedToAttendant(attendant);

        return requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(Long id){
        return requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException(id));
    }

    public Request completeRequest(Long id)
    {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));

        request.setStatus(RequestStatus.COMPLETED);

        attendantService.onAttendantRequestFinished(request.getAttendant());

        return requestRepository.save(request);
    }

    public Request cancelRequest(Long id){
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));

        if(request.getStatus() != RequestStatus.IN_PROGRESS){
            throw new InvalidRequestStatusException(request.getStatus());
        }

        request.setStatus(RequestStatus.CANCELED);

        attendantService.onAttendantRequestFinished(request.getAttendant());

        return requestRepository.save(request);
    }

    public boolean tryToAssignQueuedRequestToAttendant(Attendant attendant)
    {
        Team team = attendant.getTeam();

        if (requestQueues.isEmpty() || requestQueues.get(team).isEmpty()) return false;

        Request request = requestQueues.get(team).poll();
        request.setAttendant(attendant);
        request.setStatus(RequestStatus.IN_PROGRESS);

        requestRepository.save(request);
        attendantService.onRequestAssignedToAttendant(attendant);

        log.info(String.format("assigned request %d to attendant %s from queue, new size: %d", request.getId(), attendant.getName(), requestQueues.get(team).size()));

        return true;
    }

    private void addRequestToQueue(Request request) throws InterruptedException {
        if (!requestQueues.containsKey(request.getTeam())){
            requestQueues.put(request.getTeam(), new ArrayBlockingQueue<>(100));
        }

        requestQueues.get(request.getTeam()).add(request);

        log.info(String.format("queue size: %d", requestQueues.get(request.getTeam()).size()));
    }

}
