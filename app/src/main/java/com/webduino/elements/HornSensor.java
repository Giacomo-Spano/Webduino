package com.webduino.elements;


import android.app.Fragment;

import com.webduino.fragment.cardinfo.DoorSensorCardInfo;
import com.webduino.fragment.cardinfo.HornSensorCardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;

import org.json.JSONObject;

public class HornSensor extends Sensor {

    private boolean alarmactivestatus;

    public HornSensor(JSONObject json) {
        super(json);

        try {
            if (json.has("alarmactivestatus"))
                alarmactivestatus = json.getBoolean("alarmactivestatus");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SensorCardInfo getCardInfo(Fragment context) {

        HornSensorCardInfo cardInfo = new HornSensorCardInfo();
        super.getCardInfo(context, cardInfo);

        cardInfo.openStatus = getAlarmActiveStatus();

        return cardInfo;
    }


    public boolean getAlarmActiveStatus() {
        return alarmactivestatus;
    }

    @Override
    void fromJson(JSONObject json) {

        super.fromJson(json);

        try {
            if (json.has("status"))
                alarmactivestatus = json.getBoolean("status");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
