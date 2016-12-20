package com.webduino;

import org.json.JSONObject;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class HeaterActuator extends Actuator {

    protected boolean releStatus;
    protected double avTemperature;
    protected double temperature;
    protected double remoteTemperature;
    protected int duration;
    protected int remaining;
    protected boolean localSensor;
    protected double targetTemperature;
    protected int activeProgramID;
    protected int activeTimeRangeID;

    public HeaterActuator() {
    }

    public double getTarget() {
        return targetTemperature;
    }

    public boolean getReleStatus() {
        return releStatus;
    }

    @Override
    void fromJson(JSONObject json) {

        super.fromJson(json);

        try {
            if (json.has("target"))
                targetTemperature = json.getDouble("target");
            if (json.has("relestatus"))
                releStatus = json.getBoolean("relestatus");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
