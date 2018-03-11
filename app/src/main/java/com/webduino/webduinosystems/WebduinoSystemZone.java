package com.webduino.webduinosystems;

import com.webduino.elements.Sensor;
import com.webduino.zones.Zone;
import com.webduino.zones.Zones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class WebduinoSystemZone {

    public int id;
    public String name;
    public Zone zone;

    public WebduinoSystemZone(JSONObject jsonObject) throws JSONException {
        fromJson(jsonObject);
    }

    public void fromJson(JSONObject jObject) throws JSONException {

        if (jObject.has("id"))
            id = jObject.getInt("id");
        if (jObject.has("name"))
            name = jObject.getString("name");
        if (jObject.has("zoneid")) {
            int zoneid = jObject.getInt("zoneid");
            zone = Zones.getFromId(zoneid);
        }
    }
}
