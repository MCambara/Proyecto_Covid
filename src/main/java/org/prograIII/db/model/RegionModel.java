package org.prograIII.db.model;

public class RegionModel {
    private int id;
    private String iso;
    private String name;

    // Constructor
    public RegionModel(int id, String iso, String name) {
        this.id = id;
        this.iso = iso;
        this.name = name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RegionModel{id=" + id + ", iso='" + iso + "', name='" + name + "'}";
    }
}
