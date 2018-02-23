package com.webduino.scenarios;

import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class ProgramAction {



    public int id = 0;
    public int timerangeid = 0;
    public String type = "";
    public String name = "";
    public String description = "";
    public int priority = 0;
    public int actuatorid = 0;
    public double targetvalue = 0;
    public double thresholdvalue = 0;
    public int zoneid = 0;
    public int seconds = 0;
    public boolean enabled = true;

    protected boolean hasZone = false;
    protected boolean hasThreshold = false;
    protected boolean hasActuator = false;
    protected boolean hasTarget = false;
    protected boolean hasDuration = false;

    public ProgramAction() {

    }

    public ProgramAction(int id, int programtimerangeid, String type, String name, String description, int priority, int actuatorid, double targevalue, double thresholdvalue, int zoneId, int seconds, boolean enabled) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.actuatorid = actuatorid;
        this.targetvalue = targevalue;
        this.thresholdvalue = thresholdvalue;
        this.zoneid = zoneId;
        this.seconds = seconds;
        this.enabled = enabled;


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


    /*public void fromJson(JSONObject json) throws Exception {

        if (json.has("id"))
            id = json.getInt("id");
        if (json.has("enabled"))
            enabled = json.getBoolean("enabled");
        if (json.has("name"))
            name = json.getString("name");
        if (json.has("description"))
            description = json.getString("description");
        if (json.has("type"))
            type = json.getString("type");
        if (json.has("priority"))
            priority = json.getInt("priority");
        if (json.has("actuatorid"))
            actuatorid = json.getInt("actuatorid");
        if (json.has("target"))
            targetvalue = json.getDouble("target");
        if (json.has("thresholdvalue"))
            thresholdvalue = json.getDouble("thresholdvalue");
        if (json.has("zoneid"))
            zoneid = json.getInt("zoneid");
        if (json.has("seconds"))
            seconds = json.getInt("seconds");
    }*/
}
