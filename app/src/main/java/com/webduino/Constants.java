package com.webduino;

/**
 * Created by Giacomo Spanò on 14/01/2017.
 */

import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;

public class Constants {

    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 2000;

    public static final HashMap<String, LatLng> LANDMARKS = new HashMap<String, LatLng>();
    static {
        // San Francisco International Airport.
        LANDMARKS.put("Casa", new LatLng(45.513438,9.238462));

        // Googleplex.
        LANDMARKS.put("Ufficio", new LatLng(45.457876,9.070369));

        // Test
        LANDMARKS.put("SFO", new LatLng(37.621313,-122.378955));
    }
}
