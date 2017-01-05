package com.webduino.elements;

import org.json.JSONObject;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class HeaterActuator extends Actuator {

    /*public static final String Command_Program_Off = "programoff";
    public static final String Command_Program_On = "programon";
    public static final String Command_Send_Temperature = "sendtemperature";
    public static final String Command_Manual_Auto = "manual";
    public static final String Command_Manual_End = "endmanual";
    public static final String Command_Manual_Off = "manualoff";*/

    public static final String Command_Program_ReleOff = "0"; // "programoff";
    public static final String Command_Program_ReleOn = "1"; // "programon";
    public static final String Command_Send_Disabled = "2"; // "sendtemperature";
    public static final String Command_Send_Enabled = "3"; // "sendtemperature";
    public static final String Command_Manual_Off = "4"; // "manualoff";
    public static final String Command_Manual_Auto = "5"; // "manual";
    public static final String Command_Manual_End = "6"; // "endmanual";
    public static final String Command_Send_Temperature = "7"; // "sendtemperature";

    public static final String StatusIdle = "idle";
    public static final String StatusProgram = "program";
    public static final String StatusManual = "manual";
    public static final String StatusDisabled = "disabled";
    public static final String StatusRestarted = "restarted";
    public static final String StatusManualOff = "manualoff";

    public static final int MANUALMODE_DISABLED = 0;  // modalità manuale disabilitata
    public static final int MANUALMODE_AUTO = 1;  // temperatura automatica >= alk sensorid
    public static final int MANUALMODE_OFF = 2; // sempre spento
    public static final int MANUALMODE_END = 3; // fine manual mode


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

    public int getDuration() {
        return duration;
    }

    public int getRemainig() {
        return remaining;
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
            if (json.has("remaining"))
                remaining = json.getInt("remaining");
            if (json.has("duration"))
                duration = json.getInt("duration");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
