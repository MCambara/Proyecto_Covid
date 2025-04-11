package org.prograIII.util;

public class ReportLoader {

    private final String date;
    private final int confirmed;
    private final int deaths;
    private final int recovered;
    private final String iso;
    private final String regionName;
    private final String province;

    public ReportLoader(String date, int confirmed, int deaths, int recovered,
                        String iso, String regionName, String province) {
        this.date = date;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.iso = iso;
        this.regionName = regionName;
        this.province = province;
    }

    @Override
    public String toString() {
        return "[Fecha: " + date + ", Confirmados: " + confirmed + ", Muertes: " + deaths + ", Recuperados: " + recovered +
                ", ISO: " + iso + ", Región: " + regionName + ", Provincia: " + (province.isEmpty() ? "General" : province) + "]";
    }
}
