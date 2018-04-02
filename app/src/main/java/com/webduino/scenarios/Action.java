package com.webduino.scenarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class Action {

    public int id = 0;
    public int programactionid = 0;
    public String type = "";
    public String actioncommand = "";
    public double targetvalue = 0;
    public int seconds = 0;
    public int actuatorid = 0;
    public int serviceid = 0;

    protected boolean hasZone = false;
    protected boolean hasThreshold = false;
    protected boolean hasActuator = false;
    protected boolean hasTarget = false;
    protected boolean hasDuration = false;

    protected boolean hasZoneSensorId = false;
    protected boolean hasZoneSensorStatus = false;
    protected boolean hasZoneSensorEnabled = false;
    protected boolean hasParam = false;
    protected boolean hasServiceId = false;

    public Action() {
    }

    public Action(JSONObject json) throws JSONException {
        fromJson(json);
    }

    public void fromJson(JSONObject json) throws JSONException {

        if (json.has("id")) id = json.getInt("id");
        if (json.has("programactionid")) programactionid = json.getInt("programactionid");
        if (json.has("actioncommand")) actioncommand = json.getString("actioncommand");
        if (json.has("type")) type = json.getString("type");
        if (json.has("actuatorid")) actuatorid = json.getInt("actuatorid");
        if (json.has("serviceid")) serviceid = json.getInt("serviceid");
        if (json.has("targetvalue")) targetvalue = json.getDouble("targetvalue");
        if (json.has("seconds")) seconds = json.getInt("seconds");
    }

    public boolean hasZone() {
        return hasZone;
    }

    public boolean hasThreshold() {
        return hasThreshold;
    }

    public boolean hasActuator() {
        return hasActuator;
    }

    public boolean hasTarget() {
        return hasTarget;
    }

    public boolean hasDuration() {
        return hasDuration;
    }

    public boolean hasZoneSensorId() {
        return hasZoneSensorId;
    }

    public boolean hasZoneSensorStatus() {
        return hasZoneSensorStatus;
    }

    public boolean isHasServiceId() {
        return hasServiceId;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("id", id);
        json.put("programactionid", programactionid);
        json.put("actuatorid", actuatorid);
        json.put("serviceid", serviceid);
        json.put("targetvalue", targetvalue);
        json.put("actioncommand", actioncommand);
        json.put("seconds", seconds);
        json.put("type", type);
        return json;
    }
}
