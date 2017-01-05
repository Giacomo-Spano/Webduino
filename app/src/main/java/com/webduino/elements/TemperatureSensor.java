package com.webduino.elements;


import com.webduino.elements.Sensor;

import org.json.JSONObject;

public class TemperatureSensor extends Sensor {

    private double temperature;
    private double avTemperature;

    public TemperatureSensor() {
    }

    public double getAvTemperature() {
        return avTemperature;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    void fromJson(JSONObject json) {

        super.fromJson(json);

        try {
            if (json.has("temperature"))
                temperature = json.getDouble("temperature");
            if (json.has("avtemperature"))
                avTemperature = json.getDouble("avtemperature");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
