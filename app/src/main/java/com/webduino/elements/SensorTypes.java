package com.webduino.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class SensorTypes {

    public static List<SensorType> list = new ArrayList<>();

    public static void add( SensorType sensortype) {
        list.add(sensortype);
    }

    public static void get(int index) {
        list.get(index);
    }

    public static List<String> getSensorStatusList(Sensor sensor) {

        String type = sensor.getType();
        for (SensorType sensortype:list) {
            if (sensortype.type.equals(type))
                return sensortype.statusList;
        }
        return null;
    }

}
