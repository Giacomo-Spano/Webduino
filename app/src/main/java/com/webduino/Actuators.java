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
}
