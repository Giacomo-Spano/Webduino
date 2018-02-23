package com.webduino.fragment.cardinfo;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class TemperatureSensorCardInfo extends SensorCardInfo {
    public double temperature = 0.0;
    //public boolean online = false;
    public int getType() {
        return TYPE_TEMPERATURESENSOR;
    }
}