package com.webduino.elements;

import android.app.Fragment;

//import com.webduino.fragment.cardinfo.HeaterCardInfo;
import com.webduino.fragment.cardinfo.HeaterCardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;
import com.webduino.fragment.cardinfo.TemperatureSensorCardInfo;

import org.json.JSONObject;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class HeaterActuator extends Actuator {

    public static final String Command_Program_ReleOff = "programoff"; // "programoff";
    public static final String Command_Program_ReleOn = "programon"; // "programon";
    public static final String Command_Send_Disabled = "disabled"; // "sendtemperature";
    public static final String Command_Send_Enabled = "enabled"; // "sendtemperature";
    public static final String Command_Manual = "manual"; // "manualoff
    //public static final String Command_Manual_Off = "manualoff"; // "manualoff";
    //public static final String Command_Auto = "auto"; // "manual";
    public static final String Command_Off = "off"; // "endmanual";
    public static final String Command_Send_Temperature = "sendtemperature"; // "sendtemperature";

    /*
    public static final String Command_Program_ReleOff = "0"; // "programoff";
    public static final String Command_Program_ReleOn = "1"; // "programon";
    public static final String Command_Send_Disabled = "2"; // "sendtemperature";
    public static final String Command_Send_Enabled = "3"; // "sendtemperature";
    public static final String Command_Manual_Off = "4"; // "manualoff";
    public static final String Command_Manual_Auto = "5"; // "manual";
    public static final String Command_Manual_End = "6"; // "endmanual";
    public static final String Command_Send_Temperature = "7"; // "sendtemperature";
    */

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
    protected int scenarioId = 0;

    //protected int duration;
    //protected int remaining;
    protected boolean localSensor;
    protected boolean releStatus;
    protected double avTemperature;
    protected double temperature;
    protected String lastTemperatureUpdate;
    protected String lastCommandDate;
    protected String endDate;
    protected String duration;
    protected String remaining;
    protected double targetTemperature;
    protected int activeProgramId;
    protected int activeTimeRangeId;
    protected int zoneId;
    protected String activeProgramIdName;
    protected String activeTimeRangeIdName;
    protected String sensorIdName;

    public HeaterActuator(JSONObject json) {
        super(json);
    }

    public SensorCardInfo getCardInfo(Fragment context) {

        HeaterCardInfo cardInfo = new HeaterCardInfo();
        super.getCardInfo(context, cardInfo);

        cardInfo.status = getStatus();
        cardInfo.releStatus = getReleStatus();
        cardInfo.temperature = getRemoteTemperature();
        cardInfo.target = getTarget();
        cardInfo.zone = "zona " + zoneId;

        return cardInfo;
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

    public int getZoneId() {
        return zoneId;
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

    public String getLastTemperatureUpdate() {
        return lastTemperatureUpdate;
    }

    public String getLastCommandDate() {
        return lastCommandDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean getLocalsensor() {
        return localSensor;
    }

    public boolean getReleStatus() {
        return releStatus;
    }

    public String getDuration() {
        return duration;
    }

    public String getRemainig() {
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
            if (json.has("scenario"))
                scenarioId = json.getInt("scenario");
            if (json.has("zone"))
                zoneId = json.getInt("zone");
            if (json.has("lasttemperatureupdate"))
                lastTemperatureUpdate = json.getString("lasttemperatureupdate");
            if (json.has("lastcommanddate"))
                lastCommandDate = json.getString("lastcommanddate");
            if (json.has("enddate"))
                endDate = json.getString("enddate");
            if (json.has("temperature"))
                remoteTemperature = json.getDouble("temperature");
            /*if (json.has("localsensor"))
                localSensor = json.getBoolean("localsensor");*/
            if (json.has("remaining"))
                remaining = json.getString("remaining");
            if (json.has("duration"))
                duration = json.getString("duration");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
