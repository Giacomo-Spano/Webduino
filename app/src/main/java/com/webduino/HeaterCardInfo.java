package com.webduino;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class HeaterCardInfo extends CardInfo {
    public String status = "Acceso";
    public double target = 0.0;
    public boolean releStatus;
    public HeaterActuator heater;
}