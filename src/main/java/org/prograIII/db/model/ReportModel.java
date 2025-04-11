package org.prograIII.db.model;

public class ReportModel {
    private int id; // Este se generará automáticamente en la base de datos
    private String date;
    private int confirmed;
    private int deaths;
    private int recovered;
    private String iso;
    private String regionName;
    private String province;

    // Constructor sin ID (para insertar un nuevo reporte)
    public ReportModel(String date, int confirmed, int deaths, int recovered, String iso, String regionName, String province) {
        this.date = date;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.iso = iso;
        this.regionName = regionName;
        this.province = province;
    }

    // Constructor con ID (para cuando se obtienen los datos de la base de datos)
    public ReportModel(int id, String date, int confirmed, int deaths, int recovered, String iso, String regionName, String province) {
        this.id = id;
        this.date = date;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.iso = iso;
        this.regionName = regionName;
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public String getIso() {
        return iso;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getProvince() {
        return province;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "date='" + date + '\'' +
                ", confirmed=" + confirmed +
                ", deaths=" + deaths +
                ", recovered=" + recovered +
                ", iso='" + iso + '\'' +
                ", regionName='" + regionName + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
