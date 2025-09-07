package com.ubots.flowpay;

import jakarta.persistence.*;

@Entity
public class Request {

    private @Id @GeneratedValue Long id;
    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private Attendant attendant;
    private RequestStatus status;
    private Team team;

    public Request() {}

    public Request(RequestStatus status, Team team) {
        this.status = status;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attendant getAttendant() {
        return attendant;
    }

    public void setAttendant(Attendant attendant) {
        this.attendant = attendant;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
