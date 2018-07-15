package com.webduino;

import com.webduino.zones.ZoneSensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class Device {

    public int id;
    public String name;
    //public String tokenId;
    //public Date date;

    public Device(JSONObject json) throws JSONException {
        fromJson(json);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    void fromJson(JSONObject json) throws JSONException {

        if (json.has("id"))
            id = json.getInt("id");
        if (json.has("name"))
            name = json.getString("name");
    }
}
