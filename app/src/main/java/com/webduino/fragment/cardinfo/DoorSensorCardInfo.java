package com.webduino.fragment.cardinfo;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class DoorSensorCardInfo extends SensorCardInfo {
    public boolean openStatus;

    public int getSensorType() {
        return TYPE_DOORSENSOR;
    }

}