package com.webduino.zones;

import android.app.Fragment;

import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class Zone {

    public int id;
    public String name;
    private String type;
    public List<ZoneSensor> zoneSensors = new ArrayList<>();

    private double temperature = 0.0;
    private boolean doorStatusOpen = false;
    public Date lastTemperatureUpdate = null;

    public Zone(JSONObject json) {
        fromJson(json);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    void fromJson(JSONObject json) {

        try {

            if (json.has("id"))
                id = json.getInt("id");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("type"))
                type = json.getString("type");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
