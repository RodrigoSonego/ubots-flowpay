package com.ubots.flowpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AttendantService {

    AttendantRepository attendantRepository;

    AttendantService(AttendantRepository attendantRepository) {
        this.attendantRepository = attendantRepository;
    }


    public Attendant getAttendantWithLessRequests(Team team) {
        List<Attendant> attendants = attendantRepository.findAttendantByTeam(team);

        attendants.sort(Comparator.comparingInt(Attendant::getCurrentRequestCount));

        Logger log = LoggerFactory.getLogger(AttendantController.class);

        for (Attendant attendant : attendants) {
            log.info(String.format("attendant %d has %d requests", attendant.getId(), attendant.getCurrentRequestCount()));
        }

        return attendants.get(0);
    }

}
