package com.example.badminton.Model;

public class CourtDBModel {
    private int id;
    private String name;
    private String statusCourt;
    private byte[] image;


    public CourtDBModel() {}

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}