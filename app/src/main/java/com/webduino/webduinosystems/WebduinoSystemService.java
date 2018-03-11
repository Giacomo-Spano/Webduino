package com.webduino.webduinosystems;

import com.webduino.Services;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.webduinosystems.services.Service;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class WebduinoSystemService {

    public int id;
    public String name;
    public Service service;

    public WebduinoSystemService(JSONObject jsonObject) throws JSONException {
        fromJson(jsonObject);
    }

    public void fromJson(JSONObject jObject) throws JSONException {

        if (jObject.has("id"))
            id = jObject.getInt("id");
        if (jObject.has("name"))
            name = jObject.getString("name");
        if (jObject.has("actuatorid")) {
            int actuatorid = jObject.getInt("actuatorid");
            service = Services.getFromId(actuatorid);
        }
    }
}
