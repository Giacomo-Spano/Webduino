package com.webduino.scenarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class Condition {

    public int id = 0;
    public int programactionid = 0;
    public int zoneid;
    public int zonesensorid;
    public String status;
    public double value;
    public String type;
    public String valueoperator;
    public List<String> valueOperatorList;

    protected boolean hasZone = true;
    protected boolean hasThreshold = false;
    protected boolean hasActuator = false;
    protected boolean hasTarget = false;
    protected boolean hasDuration = false;

    protected boolean hasZoneSensorId = false;
    protected boolean hasZoneSensorStatus = false;
    protected boolean hasZoneSensorEnabled = false;
    protected boolean hasParam = false;
    protected boolean hasServiceId = false;



    public Condition() {

    }

    public Condition(JSONObject json) throws JSONException {
        fromJson(json);
    }

    public void fromJson(JSONObject json) throws JSONException {

        if (json.has("id")) id = json.getInt("id");
        if (json.has("programactionid")) programactionid = json.getInt("timerangeid");
        if (json.has("zoneid")) zoneid = json.getInt("zoneid");
        if (json.has("zonesensorid")) zonesensorid = json.getInt("zonesensorid");
        if (json.has("status")) status = json.getString("status");
        if (json.has("value")) value = json.getDouble("value");
        if (json.has("type")) type = json.getString("type");
        if (json.has("valueoperator")) valueoperator = json.getString("valueoperator");

        if (json.has("valueoperators")) {
            JSONArray jarray = json.getJSONArray("valueoperators");
            if (jarray != null) {
                valueOperatorList = new ArrayList<>();
                for (int i = 0; i < jarray.length();i++) {
                    valueOperatorList.add((String) jarray.get(i));
                }
             }
        }
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
        json.put("zoneid", zoneid);
        json.put("zonesensorid", zonesensorid);
        json.put("status", status);
        json.put("type", type);
        json.put("value", value);
        json.put("valueoperator", valueoperator);
        return json;
    }
}