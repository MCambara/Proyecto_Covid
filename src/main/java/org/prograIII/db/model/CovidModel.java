package org.prograIII.db.model;

import java.util.Date;

public class CovidModel {
    private int reportId;
    private Date date;
    private String city;
    private String province;
    private String data;

    // Constructores
    public CovidModel() {}

    public CovidModel(int reportId, Date date, String city, String province, String data) {
        this.reportId = reportId;
        this.date = date;
        this.city = city;
        this.province = province;
        this.data = data;
    }

    // Getters y Setters (como los tienes)
    public int getReportId() {
        return reportId;
    }
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}