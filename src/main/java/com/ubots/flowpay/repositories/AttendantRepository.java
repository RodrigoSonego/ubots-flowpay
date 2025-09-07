package com.ubots.flowpay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendantRepository extends JpaRepository<Attendant, Integer> {
    List<Attendant> findAttendantByTeam(Team team);
}
