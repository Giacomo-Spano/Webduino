package com.webduino;

public class GeofenceZone {
    public String description;
    public double latitude;
    public double longitude;
    public float radius; // in metri

    public GeofenceZone(String description, double latitude, double longitude, float radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.radius = radius;
    }
}
