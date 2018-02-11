package com.webduino.scenarios;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class ScenarioTrigger {
    public int id;
    public int scenarioid;
    public int triggerid;
    public String type;
    public String name;
    public String description;
    public boolean enabled;
    //public boolean status;
    public int priority;

    public boolean active = false;

    public void fromJson(JSONObject json) throws JSONException {

        if (json.has("id")) id = json.getInt("id");
        if (json.has("scenarioid")) scenarioid = json.getInt("scenarioid");
        if (json.has("triggerid")) triggerid = json.getInt("triggerid");
        if (json.has("name")) name = json.getString("name");
        if (json.has("description")) description = json.getString("description");
        if (json.has("type")) type = json.getString("type");
        if (json.has("enabled")) enabled = json.getBoolean("enabled");
        //if (json.has("status")) status = json.getBoolean("status");
        if (json.has("priority")) priority = json.getInt("priority");
    }
}
