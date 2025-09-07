package com.ubots.flowpay.repositories;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.Request;
import com.ubots.flowpay.RequestStatus;
import com.ubots.flowpay.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findByTeam(Team team);
    List<Request> findByStatus(RequestStatus status);
    List<Request> findByAttendant(Attendant attendant);
}
