package com.ubots.flowpay.services;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.Team;
import com.ubots.flowpay.controllers.AttendantController;
import com.ubots.flowpay.exceptions.AttendantNotFoundException;
import com.ubots.flowpay.repositories.AttendantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AttendantService {

    Logger log = LoggerFactory.getLogger(AttendantController.class);

    AttendantRepository attendantRepository;

    HashMap<Team, PriorityQueue<Attendant>> attendantsList;

    AttendantService(AttendantRepository attendantRepository) {
        this.attendantRepository = attendantRepository;

        attendantsList = new HashMap<>();
    }

    public List<Attendant> getAllAttendants() {
        return attendantRepository.findAll();
    }

    public Attendant getAttendantById(int id) {
        return attendantRepository.findById(id).orElseThrow(() -> new AttendantNotFoundException(id));
    }

    public List<Attendant> getAttendantsByTeam(int team){
        return attendantRepository.findAttendantByTeam(Team.fromInt(team));
    }

    public Attendant createAttendant(Attendant attendant) {
        return attendantRepository.save(attendant);
    }

    public Attendant getAttendantWithLessRequests(Team team) {

        // Cacheia a lista se já não tem
        if (!attendantsList.containsKey(team)) {
           sortAttendantsInTeamAndAddToCache(team);
        }

        PriorityQueue<Attendant> attendants = attendantsList.get(team);

        Attendant attendant = attendants.poll();

        if (attendant != null && attendant.isBusy()){
            return null;
        }
        
        return attendant;
    }

    public void DecrementAttendantRequestCount(Attendant attendant) {
        attendant.decrementRequestCount();

        attendantRepository.save(attendant);
    }

    private void sortAttendantsInTeamAndAddToCache(Team team) {
        PriorityQueue<Attendant> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Attendant::getCurrentRequestCount));

        priorityQueue.addAll(attendantRepository.findAttendantByTeam(team));

        attendantsList.put(team, priorityQueue);

        for (Attendant at : attendantsList.get(team)) {
            log.info(String.format("attendant %s has %d requests", at.getName(), at.getCurrentRequestCount()));
        }
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

        attendant.decrementRequestCount();
        attendantRepository.save(attendant);

        attendantsList.get(attendant.getTeam()).add(attendant);
    }

}
