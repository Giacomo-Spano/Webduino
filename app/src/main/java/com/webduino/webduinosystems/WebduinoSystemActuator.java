package com.webduino.webduinosystems;

import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.zones.Zones;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class WebduinoSystemActuator {

    public int id;
    public String name;
    //public int actuator;
    public int actuatorid;

    public WebduinoSystemActuator(JSONObject jsonObject) throws Exception {
        fromJson(jsonObject);
    }

    public void fromJson(JSONObject jObject) throws Exception {

        if (jObject.has("id"))
            id = jObject.getInt("id");
        if (jObject.has("name"))
            name = jObject.getString("name");
        if (jObject.has("actuatorid")) {
            actuatorid = jObject.getInt("actuatorid");
        }
    }
}
