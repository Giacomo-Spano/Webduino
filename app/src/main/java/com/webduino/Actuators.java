package com.webduino;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class Actuators {

    public static List<Actuator> list = new ArrayList<>();

    public static void add(Actuator actuator) {
        list.add(actuator);
    }

    public static void get(int index) {
        list.get(index);
    }

    public static Actuator getFromId(int id) {
        for(Actuator actuator : list) {
            if (actuator.id == id) {
                return actuator;
            }
        }
        return null;
    }

    public static void update(Actuator actuator) {

        int index = 0;
        for (Actuator actr: list) {

            try {
                if (actuator.id == actr.id) {
                    list.set(index,actuator);
                }
                return;
            } catch (ClassCastException e) {

            }
            index++;
        }
    }
}
