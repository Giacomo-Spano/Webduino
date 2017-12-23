package com.webduino.scenarios;

import com.webduino.zones.Zone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class Scenarios {

    public static List<Scenario> list = new ArrayList<>();

    public static void add(Scenario scenario) {
        list.add(scenario);
    }

    public static void get(int index) {
        list.get(index);
    }


}
