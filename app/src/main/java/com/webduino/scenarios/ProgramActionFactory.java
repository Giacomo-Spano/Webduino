package com.webduino.scenarios;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class ProgramActionFactory {
    public ProgramAction createProgramAction(int id, int programtimerangeid, String type, String name, String description, int priority, int actuatorid, double targevalue, double thresholdvalue,
                                             int zoneId, int seconds, boolean enabled) throws Exception {
        ProgramAction programActions = null;
        if (type.equals("delayalarm")) {
            programActions = new DelayAlarmProgramActions(id, programtimerangeid, type, name, description, priority, actuatorid, targevalue, thresholdvalue,
                    zoneId, seconds, enabled);
        } else if (type.equals("keeptemperature")) {
            programActions = new KeepTemperatureProgramAction(id, programtimerangeid, type, name, description, priority, actuatorid, targevalue, thresholdvalue,
                    zoneId, seconds, enabled);
        } else if (type.equals("keepoff")) {
            programActions = new KeepOffProgramActions(id, programtimerangeid, type, name, description, priority, actuatorid, targevalue, thresholdvalue,
                    zoneId, seconds, enabled);
        } else if (type.equals("immediatealarm") || type.equals("perimetrale") || type.equals("path") || type.equals("24hours")) {
            programActions = new ProgramAction(id, programtimerangeid, type, name, description, priority, actuatorid, targevalue, thresholdvalue,
                    zoneId, seconds, enabled);
        } else if (type.equals("instruction")) { // istruzione generica vuota per inserimento nuoiva
            programActions = new ProgramAction(id, programtimerangeid, type, name, description, priority, actuatorid, targevalue, thresholdvalue,
                    zoneId, seconds, enabled);
        } else {
            throw new Exception("type:" + type + "does not exist");
        }

        //programActions.init();
        return programActions;
    }

    public ProgramAction fromJson(JSONObject json) throws Exception {

        String type = "";
        if (json.has("type"))
            type = json.getString("type");
        else
            throw new JSONException("type key missing");

        int id = 0, timerangeid = 0, actuatorid = 0, zoneId = 0, seconds = 0, priority = 0;
        String name = "";
        String description = "";
        double targetvalue = 0.0;
        double thresholdvalue = 0.0;
        boolean enabled = true;

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        if (json.has("id"))
            id = json.getInt("id");
        if (json.has("timerangeid")) timerangeid = json.getInt("timerangeid");
        if (json.has("name")) name = json.getString("name");
        if (json.has("description")) description = json.getString("description");
        if (json.has("priority")) priority = json.getInt("priority");
        if (json.has("actuatorid")) actuatorid = json.getInt("actuatorid");
        if (json.has("targetvalue")) targetvalue = json.getDouble("targetvalue");
        if (json.has("thresholdvalue")) thresholdvalue = json.getDouble("thresholdvalue");
        if (json.has("zoneid")) zoneId = json.getInt("zoneid");
        if (json.has("enabled")) enabled = json.getBoolean("enabled");


        ProgramAction action = null;
        try {
            action = createProgramAction(id, timerangeid, type, name, description, priority, actuatorid, targetvalue, thresholdvalue,
                    zoneId, seconds, enabled);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return action;
    }
}
