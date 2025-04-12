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
        return "Date: " + date + ", Confirmed: " + confirmed + ", Deaths: " + deaths + ", Recovered: " + recovered +
                ", ISO: " + iso + ", Region: " + regionName + ", Province: " + (province.isEmpty() ? "General" : province);
    }
}
