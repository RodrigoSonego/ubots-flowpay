package com.ubots.flowpay;

public class Attendant {
    private int id;
    private String name;
    private final int MAX_REQUESTS = 3;
    private int currentRequestCount;
    private Team team;

    public Attendant() {}

    public Attendant(int id, String name, Team team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentRequestCount() {
        return currentRequestCount;
    }

    public void setCurrentRequestCount(int currentRequestCount) {
        this.currentRequestCount = currentRequestCount;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
