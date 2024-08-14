package com.example.badminton.Model;

public class CourtSyncModel { private int id;
    private String name;
    private String statusCourt;

    public CourtSyncModel() {}

    public CourtSyncModel(int id, String name, String statusCourt) {
        this.id = id;
        this.name = name;
        this.statusCourt = statusCourt;
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

    public String getStatusCourt() {
        return statusCourt;
    }

    public void setStatusCourt(String statusCourt) {
        this.statusCourt = statusCourt;
    }
}