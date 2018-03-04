package com.webduino.webduinosystems;

import com.webduino.zones.Zone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class WebduinoSystems {

    public static List<WebduinoSystem> list = new ArrayList<>();

    public static void add(WebduinoSystem system) {
        list.add(system);
    }

    public static void get(int index) {
        list.get(index);
    }

    public static WebduinoSystem getFromId(int id) {
        for(WebduinoSystem system: list) {
            if (system.id == id) {
                return system;
            }
        }
        return null;
    }
}
