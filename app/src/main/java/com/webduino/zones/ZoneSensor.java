package com.webduino.zones;

import org.json.JSONObject;

/**
 * Created by giaco on 16/12/2017.
 */

public class ZoneSensor {

    public String name;
    public int id;
    public int sensorId;

    public ZoneSensor(JSONObject json) {
        fromJson(json);
    }

    void fromJson(JSONObject json) {

        try {

            if (json.has("id"))
                id = json.getInt("id");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("sensorid"))
                sensorId = json.getInt("sensorid");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
