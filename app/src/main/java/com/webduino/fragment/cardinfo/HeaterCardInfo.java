package com.webduino.fragment.cardinfo;

import com.webduino.elements.HeaterActuator;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class HeaterCardInfo extends SensorCardInfo {
    //public int actuatorId;

    public String status = "---";
    public double target = 0.0;
    public double temperature = 0.0;
    public boolean releStatus;
    public String zone = "";

    public boolean hideTarget = false;

    public HeaterActuator heater;
    public double sensorTemperature = 0.0;
    public String sensorName;

    /*public void copyFrom(HeaterActuator actuator) {
        status = actuator.getStatus();
        target = actuator.getTarget();
        releStatus = actuator.getReleStatus();
        heater = actuator;
    }*/


    public int getSensorType() {
        return TYPE_HEATER;
    }
}