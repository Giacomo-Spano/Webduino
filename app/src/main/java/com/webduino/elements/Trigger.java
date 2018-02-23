package com.webduino.elements;

import org.json.JSONObject;

/**
 * Created by giaco on 17/02/2018.
 */

public class Trigger {

    public int id = 0;
    public String name = "";
    public boolean status = false;
    public java.util.Date date;

    public void fromJson(JSONObject json) throws Exception {

        if (json.has("id"))
            id = json.getInt("id");
        if (json.has("name"))
            name = json.getString("name");
        if (json.has("status"))
            status = json.getBoolean("status");
    }
}
