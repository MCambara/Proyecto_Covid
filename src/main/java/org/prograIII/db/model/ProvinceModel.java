package org.prograIII.db.model;

public class ProvinceModel {
    private int id; // Autoincremental, no se necesita en el constructor
    private String iso;
    private String province;
    private String name;
    private double lat;
    private double lng;

    // Constructor sin el campo id
    public ProvinceModel(String iso, String province, String name, double lat, double lng) {
        this.iso = iso;
        this.province = province;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "ProvinceModel{" +
                "id=" + id +
                ", iso='" + iso + '\'' +
                ", province='" + province + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
