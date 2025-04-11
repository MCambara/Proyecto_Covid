package org.prograIII.util;

public class ProvinceLoader {

    private final String iso;
    private final String province;
    private final String name;
    private final double lat;
    private final double lng;

    public ProvinceLoader(String iso, String province, String name, double lat, double lng) {
        this.iso = iso;
        this.province = province;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getIso() {
        return iso;
    }

    public String getProvince() {
        return province;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return name + " (" + province + ") - " + lat + ", " + lng;
    }
}
