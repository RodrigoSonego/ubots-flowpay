package com.ubots.flowpay.services;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.Team;
import com.ubots.flowpay.controllers.AttendantController;
import com.ubots.flowpay.exceptions.AttendantNotFoundException;
import com.ubots.flowpay.exceptions.InvalidAttendantException;
import com.ubots.flowpay.exceptions.InvalidTeamOrdinalException;
import com.ubots.flowpay.repositories.AttendantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AttendantService {

    private final RequestService requestService;
    Logger log = LoggerFactory.getLogger(AttendantController.class);

    AttendantRepository attendantRepository;

    HashMap<Team, PriorityQueue<Attendant>> attendantsList;

    AttendantService(AttendantRepository attendantRepository, @Lazy RequestService requestService) {
        this.attendantRepository = attendantRepository;

        attendantsList = new HashMap<>();
        this.requestService = requestService;
    }

    public List<Attendant> getAllAttendants() {
        return attendantRepository.findAll();
    }

    public Attendant getAttendantById(int id) {
        return attendantRepository.findById(id).orElseThrow(() -> new AttendantNotFoundException(id));
    }

    public List<Attendant> getAttendantsByTeam(int team) {
        if(!Team.isValidOrdinal(team)){
            throw new InvalidTeamOrdinalException(team);
        }

        return attendantRepository.findAttendantByTeam(Team.fromInt(team));
    }

    public Attendant createAttendant(Attendant attendant) {
        if(attendant.getTeam() == null || attendant.getName() == null){
            throw new InvalidAttendantException(attendant.getName());
        }

        attendantRepository.save(attendant);
        addAttendantToMap(attendant);

        return attendant;
    }

    public Attendant getAttendantWithLessRequests(Team team) {

        PriorityQueue<Attendant> attendants = attendantsList.get(team);

        Attendant attendant = attendants.poll();

        if (attendant != null && attendant.isBusy()){
            return null;
        }

        return attendant;
    }

    public void decrementAttendantRequestCount(Attendant attendant) {
        attendant.decrementRequestCount();

        attendantRepository.save(attendant);
    }


    public void onRequestAssignedToAttendant(Attendant attendant) {
        attendantsList.get(attendant.getTeam()).remove(attendant);

        attendant.incrementRequestCount();
        attendantRepository.save(attendant);

        attendantsList.get(attendant.getTeam()).add(attendant);

        for (Attendant at : attendantsList.get(attendant.getTeam())) {
            log.info(String.format("attendant %s has %d requests", at.getName(), at.getCurrentRequestCount()));
        }
    }

    public void onAttendantRequestFinished(Attendant attendant) {
        attendantsList.get(attendant.getTeam()).remove(attendant);

        decrementAttendantRequestCount(attendant);

        // Se conseguir pegar uma request da fila, onRequestAssigned vai ser chamado e
        // cuida da reogranização da fila
        if (requestService.tryToAssignQueuedRequestToAttendant(attendant)){
            return;
        }

        attendantsList.get(attendant.getTeam()).add(attendant);
    }

    private void addAttendantToMap(Attendant attendant) {
        Team team = attendant.getTeam();

        if (!attendantsList.containsKey(team)) {
            PriorityQueue<Attendant> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Attendant::getCurrentRequestCount));
            attendantsList.put(team, priorityQueue);
        }

        attendantsList.get(team).add(attendant);
    }

}
