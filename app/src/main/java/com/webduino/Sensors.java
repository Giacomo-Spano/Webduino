package com.webduino;

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
}
