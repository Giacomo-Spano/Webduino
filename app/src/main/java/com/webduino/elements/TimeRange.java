package com.webduino.elements;
import java.sql.Time;

/**
 * Created by Giacomo Span� on 07/11/2015.
 */
public class TimeRange {

    public int ID;
    public String name;
    public Time endTime;
    public Time starTime;
    public Double temperature = 0.0;
    public int sensorId = 0;
    public int programID;
    public int priority;

    public TimeRange() {
    }

    public TimeRange(int ID, String name, Time endTime, Double temperature, int shieldId, int programID) {
        this.ID = ID;
        this.name = name;
        this.endTime = endTime;
        this.temperature = temperature;
        this.sensorId = shieldId;
        this.programID = programID;
    }

    public TimeRange(TimeRange tr) {
        this.ID = tr.ID;
        this.name = tr.name;
        this.endTime = tr.endTime;
        this.temperature = tr.temperature;
        this.sensorId = tr.sensorId;
        this.programID = tr.programID;
    }
}
