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

    protected double remoteTemperature;
    protected int duration;
    protected int remaining;
    protected boolean localSensor;
    protected boolean releStatus;
    protected double avTemperature;
    protected double temperature;
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
