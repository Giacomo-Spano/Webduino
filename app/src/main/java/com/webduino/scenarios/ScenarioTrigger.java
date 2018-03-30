package com.webduino.scenarios;

import com.webduino.elements.Trigger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

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

    public ScenarioTrigger(JSONObject json) throws JSONException {
        fromJson(json);
    }

    public ScenarioTrigger() {
    }

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

    public JSONObject toJson() throws JSONException {

        JSONObject json = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        json.put("id", id);
        json.put("triggerid", triggerid);
        json.put("scenarioid", scenarioid);
        json.put("name", name);
        json.put("description", description);
        json.put("type", type);
        json.put("enabled", enabled);
        json.put("priority", priority);

        return json;
    }
}
