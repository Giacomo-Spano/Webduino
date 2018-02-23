package com.webduino.elements;

import com.webduino.elements.Trigger;
import com.webduino.scenarios.ProgramAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class ProgramActionTypes {

    public static List<ProgramActionType> list = new ArrayList<>();

    public static void add( ProgramActionType actiontype) {
        list.add(actiontype);
    }

    public static void get(int index) {
        list.get(index);
    }

    /*public static ProgramActionType getFromId(int id) {
        for(ProgramActionType actiontype : list) {
            if (actiontype.id == id) {
                return actiontype;
            }
        }
        return null;
    }*/
}
