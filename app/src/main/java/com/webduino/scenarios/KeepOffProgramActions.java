package com.webduino.scenarios;

/**
 * Created by gs163400 on 10/02/2018.
 */

class KeepOffProgramActions extends ProgramAction {
    public KeepOffProgramActions(int id, int programtimerangeid, String type, String name, String description, int priority, int actuatorid, double targevalue, double thresholdvalue, int zoneId, int seconds, boolean enabled) {
        super(id,programtimerangeid,type,name,description,priority,actuatorid,targevalue,thresholdvalue,zoneId,seconds,enabled);
    }
}
