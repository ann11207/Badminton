package com.example.badminton.Model;

public class CatalogDBModel {
    private int catalogId;
    private String name;
    private String description;



    public CatalogDBModel(){}

    public CatalogDBModel(int catalogId, String name, String description) {
        this.catalogId = catalogId;
        this.name = name;
        this.description = description;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
