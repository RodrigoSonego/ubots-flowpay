package com.ubots.flowpay.repositories;

import com.ubots.flowpay.Attendant;
import com.ubots.flowpay.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendantRepository extends JpaRepository<Attendant, Integer> {
    List<Attendant> findAttendantByTeam(Team team);
}
