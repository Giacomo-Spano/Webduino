package com.webduino.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class NextPrograms {

    public static List<NextProgram> list = new ArrayList<>();

    public static void add(NextProgram program) {
        list.add(program);
    }

    public static void get(int index) {
        list.get(index);
    }
}
