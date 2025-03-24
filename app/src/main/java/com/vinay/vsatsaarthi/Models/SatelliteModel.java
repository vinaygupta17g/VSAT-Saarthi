package com.vinay.vsatsaarthi.Models;

public class SatelliteModel {
    String id,SatelliteName,longitude,latetude,altitude;

    public SatelliteModel(String id, String satelliteName, String longitude, String latetude, String altitude) {
        this.id = id;
        SatelliteName = satelliteName;
        this.longitude = longitude;
        this.latetude = latetude;
        this.altitude = altitude;
    }
    public SatelliteModel(String satelliteName, String longitude, String latetude, String altitude) {
        SatelliteName = satelliteName;
        this.longitude = longitude;
        this.latetude = latetude;
        this.altitude = altitude;
    }

    public SatelliteModel() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSatelliteName() {
        return SatelliteName;
    }

    public void setSatelliteName(String satelliteName) {
        SatelliteName = satelliteName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatetude() {
        return latetude;
    }

    public void setLatetude(String latetude) {
        this.latetude = latetude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }
}
