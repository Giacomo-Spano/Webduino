package com.webduino;

import java.util.ArrayList;
import java.util.List;

public class GeofenceZones {
    public List<GeofenceZone> zones = new ArrayList<>();

    public GeofenceZones() {
        zones.add(new GeofenceZone("Casa", 45.513438,9.238462, 2000));
        zones.add(new GeofenceZone("Ufficio", 45.457876,9.070369, 2000));
    }
}
