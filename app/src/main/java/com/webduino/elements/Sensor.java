package com.webduino.elements;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class Sensor {

    protected int id;
    protected int shieldid;
    protected String onlinestatus;
    protected String subaddress;
    protected String name;
    protected Date lastUpdate;

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
            if (json.has("shieldid"))
                shieldid = json.getInt("shieldid");
            if (json.has("onlinestatus"))
                onlinestatus = json.getString("onlinestatus");
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
