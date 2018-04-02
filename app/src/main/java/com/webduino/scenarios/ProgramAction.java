package com.webduino.scenarios;

import org.json.JSONArray;
import org.json.JSONException;
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
    public int serviceid;
    public String param;
    public int zonesensorid;
    public String zonesensorstatus;
    public List<Condition> conditions = new ArrayList<>();
    public List<Action> actions = new ArrayList<>();

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

    public ProgramAction() {

    }

    public ProgramAction(int id, int timerangeid, String type, String name, String description, int priority, int actuatorid, double targevalue, double thresholdvalue, int zoneId, int seconds, boolean enabled) {
        this.id = id;
        this.timerangeid = timerangeid;
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

    public ProgramAction(JSONObject json) throws JSONException {
        fromJson(json);
    }

    public void fromJson(JSONObject json) throws JSONException {

        if (json.has("id")) id = json.getInt("id");
        if (json.has("timerangeid")) timerangeid = json.getInt("timerangeid");
        if (json.has("type")) type = json.getString("type");
        if (json.has("name")) name = json.getString("name");
        if (json.has("description")) description = json.getString("description");
        if (json.has("actuatorid")) actuatorid = json.getInt("actuatorid");
        if (json.has("targetvalue")) targetvalue = json.getDouble("targetvalue");
        if (json.has("thresholdvalue")) thresholdvalue = json.getDouble("thresholdvalue");
        if (json.has("zoneid")) zoneid = json.getInt("zoneid");
        if (json.has("seconds")) seconds = json.getInt("seconds");
        if (json.has("enabled")) enabled = json.getBoolean("enabled");
        if (json.has("priority")) priority = json.getInt("priority");
        if (json.has("serviceid")) serviceid = json.getInt("serviceid");
        if (json.has("param")) param = json.getString("param");
        if (json.has("zonesensorid")) zonesensorid = json.getInt("zonesensorid");
        if (json.has("zonesensorstatus")) zonesensorstatus = json.getString("zonesensorstatus");
        if (json.has("actions")) {
            JSONArray jsonArray = json.getJSONArray("actions");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonAction = jsonArray.getJSONObject(i);
                    Action action = new Action(jsonAction);
                    if (action != null)
                        actions.add(action);
                }
            }
        }
        if (json.has("conditions")) {
            JSONArray jsonArray = json.getJSONArray("conditions");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonCondition = jsonArray.getJSONObject(i);
                    Condition condition = new Condition(jsonCondition);
                    if (condition != null)
                        conditions.add(condition);
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
        json.put("timerangeid", timerangeid);
        json.put("type", type);
        json.put("name", name);
        json.put("description", description);
        json.put("actuatorid", actuatorid);
        json.put("targetvalue", targetvalue);
        json.put("thresholdvalue", thresholdvalue);
        json.put("zoneid", zoneid);
        json.put("seconds", seconds);
        json.put("enabled", enabled);
        json.put("priority", priority);
        json.put("serviceid", serviceid);
        json.put("param", param);
        json.put("zonesensorid", zonesensorid);
        json.put("zonesensorstatus", zonesensorstatus);

        return json;
    }
}
