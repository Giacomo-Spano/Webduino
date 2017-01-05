package com.webduino.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class Programs {

    public static List<Program> list = new ArrayList<>();

    public static void add(Program program) {
        list.add(program);
    }

    public static void get(int index) {
        list.get(index);

    }

    public static Program getFromId(int id) {
        for(Program program : list) {
            if (program.id == id) {
                return program;
            }
        }
        return null;
    }

    /*public static void update(Program program) {

        int index = 0;
        for (Program prgm: list) {

            try {
                if (program.id == prgm.id) {
                    list.set(index,program);
                }
                return;
            } catch (ClassCastException e) {

            }
            index++;
        }
    }*/
}
