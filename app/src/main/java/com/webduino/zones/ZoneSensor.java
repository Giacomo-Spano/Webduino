package com.webduino.zones;

import org.json.JSONObject;

/**
 * Created by giaco on 16/12/2017.
 */

public class ZoneSensor {

    public String name;
    int id;
    private int sensorId;

    public ZoneSensor(JSONObject json) {
        fromJson(json);
    }

    void fromJson(JSONObject json) {

        try {

            if (json.has("id"))
                id = json.getInt("id");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("sensor"))
                sensorId = json.getInt("sensor");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
