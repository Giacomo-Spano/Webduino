package com.webduino.elements;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Giacomo Span� on 07/11/2015.
 */
public class CommandDataLog {

    public Date date;
    public Date enddate;

    public int duration;
    public double target;
    public double temperature;
    public String command;
    public boolean success;

    public CommandDataLog() {
    }

    public void fromJson(JSONObject jsonObj) throws Exception {
        if (jsonObj.has("date")) {
            String strdate = jsonObj.getString("date");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = df.parse(strdate);
        }
        if (jsonObj.has("duration"))
            duration = jsonObj.getInt("duration");
        if (jsonObj.has("target"))
            target = jsonObj.getDouble("target");
        if (jsonObj.has("temperature"))
            temperature = jsonObj.getDouble("temperature");
        if (jsonObj.has("command"))
            command = jsonObj.getString("command");
        if (jsonObj.has("success"))
            success = jsonObj.getBoolean("success");
        if (jsonObj.has("enddate")) {
            String strdate = jsonObj.getString("date");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            enddate = df.parse(strdate);
        }
    }

}
