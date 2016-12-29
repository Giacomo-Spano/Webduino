package com.webduino;

import org.json.JSONObject;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class HeaterActuator extends Actuator {

    public static final String Command_Program_On = "program_on";
    //public static final String Command_Send_Temperature = 2;
    public static final String Command_Manual_On = "manual_on"; // on e off differiscono per la temperatura
    public static final String Command_Manual_Off = "manual_off";

    public static final String StatusIdle = "idle";
    public static final String StatusProgram = "program";
    public static final String StatusManual = "manual";
    public static final String StatusDisabled = "disabled";
    public static final String StatusRestarted = "restarted";

    protected double remoteTemperature;
    protected int duration;
    protected int remaining;
    protected boolean localSensor;
    protected boolean releStatus;
    protected double avTemperature;
    protected double temperature;
    protected double targetTemperature;
    protected int activeProgramId;
    protected int activeTimeRangeId;
    protected int sensorId;

    protected String activeProgramIdName;
    protected String activeTimeRangeIdName;
    protected String sensorIdName;

    public HeaterActuator() {
    }

    public double getTarget() {
        return targetTemperature;
    }

    public int getActiveProgramId() {
        return activeProgramId;
    }

    public int getActiveTimeRangeId() {
        return activeTimeRangeId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public String getActiveProgramIdName() {
        return activeProgramIdName;
    }

    public String getActiveTimeRangeIdName() {
        return activeTimeRangeIdName;
    }

    public String getSensorIdName() {
        return sensorIdName;
    }

    public double getRemoteTemperature() {
        return remoteTemperature;
    }

    public boolean getLocalsensor() {
        return localSensor;
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

            if (json.has("program"))
                activeProgramId = json.getInt("program");
            if (json.has("programname"))
                activeProgramIdName = json.getString("programname");
            if (json.has("timerange"))
                activeTimeRangeId = json.getInt("timerange");
            if (json.has("timerangename"))
                activeTimeRangeIdName = json.getString("timerangename");
            if (json.has("sensorID"))
                sensorId = json.getInt("sensorID");
            if (json.has("sensorIDname"))
                sensorIdName = json.getString("sensorIDname");
            if (json.has("remotetemperature"))
                remoteTemperature = json.getDouble("remotetemperature");
            if (json.has("localsensor"))
                localSensor = json.getBoolean("localsensor");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
