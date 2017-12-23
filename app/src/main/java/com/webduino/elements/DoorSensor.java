package com.webduino.elements;


import android.app.Fragment;

import com.webduino.fragment.cardinfo.DoorSensorCardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;

import org.json.JSONObject;

public class DoorSensor extends Sensor {

    private boolean open;

    public DoorSensor(JSONObject json) {
        super(json);

        try {
            if (json.has("openstatus"))
                open = json.getBoolean("openstatus");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SensorCardInfo getCardInfo(Fragment context) {

        DoorSensorCardInfo cardInfo = new DoorSensorCardInfo();
        super.getCardInfo(context, cardInfo);

        cardInfo.openStatus = getOpenStatus();

        return cardInfo;
    }


    public boolean getOpenStatus() {
        return open;
    }

    @Override
    void fromJson(JSONObject json) {

        super.fromJson(json);

        try {
            if (json.has("status"))
                open = json.getBoolean("status");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
