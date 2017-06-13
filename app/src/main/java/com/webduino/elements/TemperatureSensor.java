package com.webduino.elements;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;

import com.webduino.R;
import com.webduino.elements.Sensor;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;
import com.webduino.fragment.cardinfo.TemperatureSensorCardInfo;

import org.json.JSONObject;

public class TemperatureSensor extends Sensor {

    private double temperature;
    private double avTemperature;

    public TemperatureSensor(JSONObject json) {
        super(json);
    }

    public double getAvTemperature() {
        return avTemperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public SensorCardInfo getCardInfo(Fragment context) {

        TemperatureSensorCardInfo cardInfo = new TemperatureSensorCardInfo();
        super.getCardInfo(context, cardInfo);

        cardInfo.temperature = getTemperature();

        return cardInfo;
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
