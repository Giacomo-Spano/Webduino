package com.webduino.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class Triggers {

    public static List<Trigger> list = new ArrayList<>();

    public static void add(Trigger trigger) {
        list.add(trigger);
    }

    public static void get(int index) {
        list.get(index);
    }

    public static Trigger getFromId(int id) {
        for(Trigger trigger : list) {
            if (trigger.id == id) {
                return trigger;
            }
        }
        return null;
    }
}
