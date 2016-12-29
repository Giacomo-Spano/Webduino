package com.webduino;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class Actuator extends Sensor {


    private String status;

    public Actuator() {
    }

    public String getStatus() {
        return status;
    }


    @Override
    void fromJson(JSONObject json) {

        super.fromJson(json);

        try {
            if (json.has("status"))
                status = json.getString("status");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
