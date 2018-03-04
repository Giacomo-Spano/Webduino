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
    public Sensor actuator;

    public WebduinoSystemActuator(JSONObject jsonObject) throws JSONException {
        fromJson(jsonObject);
    }

    public void fromJson(JSONObject jObject) throws JSONException {

        if (jObject.has("id"))
            id = jObject.getInt("id");
        if (jObject.has("name"))
            name = jObject.getString("name");
        if (jObject.has("zoneid")) {
            int zoneid = jObject.getInt("id");
            actuator = Sensors.getFromId(id);
        }
    }
}
