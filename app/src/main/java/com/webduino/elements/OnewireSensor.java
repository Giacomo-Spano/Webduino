package com.webduino.elements;


import android.app.Fragment;

import com.webduino.fragment.cardinfo.DoorSensorCardInfo;
import com.webduino.fragment.cardinfo.OnewireCardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;

import org.json.JSONObject;

public class OnewireSensor extends Sensor {

    private boolean statusOpen;

    public OnewireSensor(JSONObject json) {
        super(json);
    }

    public SensorCardInfo getCardInfo(Fragment context) {

        OnewireCardInfo cardInfo = new OnewireCardInfo();
        super.getCardInfo(context, cardInfo);

        //cardInfo.openStatus = getOpenStatus();

        return cardInfo;
    }


    @Override
    void fromJson(JSONObject json) {

        super.fromJson(json);

        try {
            /*if (json.has("open"))
                statusOpen = json.getBoolean("open");*/

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
