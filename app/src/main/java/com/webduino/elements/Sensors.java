package com.webduino.elements;

import com.webduino.elements.Sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class Sensors {

    public static List<Sensor> list = new ArrayList<>();

    public static void add(Sensor sensor) {
        list.add(sensor);
    }

    public static void get(int index) {
        list.get(index);
    }

    public static Sensor getFromId(int id) {
        for(Sensor sensor : list) {
            if (sensor.id == id) {
                return sensor;
            }
        }
        return null;
    }
}
