package com.webduino.scenarios;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gs163400 on 10/02/2018.
 */

class KeepTemperatureProgramAction extends ProgramAction {
    public KeepTemperatureProgramAction(int id, int programtimerangeid, String type, String name, String description, int priority, int actuatorid, double targevalue, double thresholdvalue, int zoneId, int seconds, boolean enabled) {
        super(id,programtimerangeid,type,name,description,priority,actuatorid,targevalue,thresholdvalue,zoneId,seconds,enabled);
        hasZone = true;
        hasThreshold = true;
        hasActuator = true;
        hasTarget = true;
        hasDuration = true;
    }

    public KeepTemperatureProgramAction(JSONObject json) throws JSONException {
        super(json);
        hasZone = true;
        hasThreshold = true;
        hasActuator = true;
        hasTarget = true;
        hasDuration = true;
    }
}
