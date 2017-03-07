package com.webduino.fragment.cardinfo;

import com.webduino.elements.HeaterActuator;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class HeaterCardInfo extends CardInfo {
    public int actuatorId;

    public String status = "---";
    public double target = 0.0;
    public boolean hideTarget = false;
    public boolean releStatus;
    public HeaterActuator heater;
    public double sensorTemperature = 0.0;
    public String sensorName;

    public void copyFrom(HeaterActuator actuator) {
        status = actuator.getStatus();
        target = actuator.getTarget();
        releStatus = actuator.getReleStatus();
        heater = actuator;
    }
}