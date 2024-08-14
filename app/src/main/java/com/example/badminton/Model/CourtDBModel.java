package com.example.badminton.Model;

public class CourtDBModel {
    private int id;
    private String name;
    private String statusCourt;
    private byte[] image;


    public CourtDBModel() {}

    public CourtDBModel(int id, String name, String statusCourt, byte[] image) {
        this.id = id;
        this.name = name;
        this.statusCourt = statusCourt;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}