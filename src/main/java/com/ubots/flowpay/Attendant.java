package com.ubots.flowpay;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Attendant {

    private @Id
    @GeneratedValue int id;
    private String name;
    private final int MAX_REQUESTS = 3;
    private int currentRequestCount;
    private Team team;

    public Attendant() {}

    public Attendant(String name, Team team) {
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
