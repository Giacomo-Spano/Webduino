package com.webduino.elements;

import android.app.Fragment;
import android.content.Context;

import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;
import com.webduino.fragment.cardinfo.TemperatureSensorCardInfo;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class Sensor {

    protected int id;
    protected int shieldid;
    protected boolean online;
    protected String subaddress;
    protected String name;
    protected Date lastUpdate;

    public List<Sensor> childsensors = new ArrayList<>();

    //protected SensorCardInfo cardInfo;

    public Sensor(JSONObject json) {
        fromJson(json);
    }

    public CardInfo getCardInfo(Fragment context) {
        return getCardInfo(context, null);
    }

    public CardInfo getCardInfo(Fragment context, CardInfo cardInfo) {

        if (cardInfo == null)
            cardInfo = new SensorCardInfo();
        cardInfo.id = getId();
        cardInfo.shieldid = getShieldId();
        cardInfo.name = getName();
        cardInfo.online = getOnLine();
        cardInfo.setEnabled(true);

        return cardInfo;
    }

    public int getId() {
        return id;
    }

    public int getShieldId() {
        return shieldid;
    }

    public String getName() {
        return name;
    }

    public boolean getOnLine() {
        return online;
    }

    void fromJson(JSONObject json) {

        try {

            if (json.has("id"))
                id = json.getInt("id");
            if (json.has("shieldid"))
                shieldid = json.getInt("shieldid");
            if (json.has("online"))
                online = json.getBoolean("online");
            if (json.has("subaddress"))
                subaddress = json.getString("subaddress");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("lastupdate")) {
                String strDate = json.getString("lastupdate");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (strDate != null)
                    lastUpdate = df.parse(strDate);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
