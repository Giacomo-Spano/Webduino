package com.webduino;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionCommand {
    public String command;
    public String name;

    boolean hastarget = false;
    double mintargetvalue;
    double maxtargetvalue;
    String targetname = "Target";

    boolean haszone = false;
    String zonename;
    String zonesensortype;

    boolean hasparam = false;
    public String paramname;
    double paramlen;

    boolean hasstatus = false;
    String statusname = "stato";

    public int deviceid = 0;
    public String devicename = "";
    boolean hasdevice = false;

    public boolean hasZone() {
        return haszone;
    }

    public boolean hasParam() {
        return hasparam;
    }

    public boolean hasDevice() {
        return hasdevice;
    }

    public boolean hasStatus() {
        return hasstatus;
    }

    public boolean hasTarget() {
        return hastarget;
    }

    public ActionCommand(JSONObject json) throws JSONException {
        fromJson(json);
    }

    public String getZoneSensorType() {
        return zonesensortype;
    }

    void fromJson(JSONObject json) throws JSONException {
        if (json.has("command"))
            command = json.getString("command");
        if (json.has("name"))
            name = json.getString("name");

        if (json.has("targetvalue")) {
            hastarget = true;
            if (json.has("targetname"))
                targetname = json.getString("targetname");
            if (json.has("mintargetvalue"))
                mintargetvalue = json.getDouble("mintargetvalue");
            if (json.has("maxtargetvalue"))
                maxtargetvalue = json.getDouble("maxtargetvalue");
        }

        if (json.has("zone")) {
            haszone = true;
            if (json.has("zonename"))
                zonename = json.getString("zonename");
            if (json.has("zonesensortype"))
                zonesensortype = json.getString("zonesensortype");
        }

        if (json.has("param")) {
            hasparam = true;
            if (json.has("paramname"))
                paramname = json.getString("paramname");
            if (json.has("paramlen"))
                paramlen = json.getDouble("paramlen");
        }

        if (json.has("device")) {
            hasdevice = true;
            deviceid = json.getInt("device");
            if (json.has("devicename"))
                devicename = json.getString("devicename");
        }

        if (json.has("hasstatus")) {
            hasstatus = true;
            if (json.has("statusname"))
                statusname = json.getString("statusname");
        }
    }
}
