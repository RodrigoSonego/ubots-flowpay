package com.ubots.flowpay;

public class Request {
    private int id;
    private Attendant attendant;
    private RequestStatus status;
    private Team team;

    public Request() {}

    public Request(int id, Attendant attendant, RequestStatus status, Team team) {
        this.id = id;
        this.attendant = attendant;
        this.status = status;
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
