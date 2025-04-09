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

    // Getters, toString(), etc.
    @Override
    public String toString() {
        return name + " (" + province + ") - " + lat + ", " + lng;
    }



}
