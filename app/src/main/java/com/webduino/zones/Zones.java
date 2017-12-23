package com.webduino.zones;

import com.webduino.elements.Sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class Zones {

    public static List<Zone> list = new ArrayList<>();

    public static void add(Zone zone) {
        list.add(zone);
    }

    public static void get(int index) {
        list.get(index);
    }


}
