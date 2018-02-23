package com.webduino.fragment.cardinfo;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class OnewireCardInfo extends SensorCardInfo {
    public boolean openStatus;

    public OnewireCardInfo() {
        type = TYPE_ONEWIRESENSOR;
    }
}