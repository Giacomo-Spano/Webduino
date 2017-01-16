package com.webduino.chart;


import com.webduino.elements.TimeRange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by giacomo on 07/06/2015.
 */
public class HistoryData implements Serializable {

    public int id;
    public Date date;
    public double remoteTemperature;
    public double targetTemperature;
    public int activeProgram;
    public int activeTimeRange;
    public boolean releStatus;

    public HistoryData() {
    }

    /*public HistoryData(HistoryData md) {

        if (md == null)
            return;
        id = md.id;
        sint count = 0;
        for (String url : md.webcamurlList) {
            webcamurlList.add(url);
        }
        offline  = md.offline;
    }*/

    public void fromJson(JSONObject jObject) {

        try {
            if (jObject.has("date")) {

                String strDate;
                strDate = jObject.getString("date");
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    date = df.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (jObject.has("relestatus"))
                    releStatus = jObject.getBoolean("relestatus");
                if (jObject.has("remotetemperature"))
                    remoteTemperature = jObject.getDouble("remotetemperature");
                if (jObject.has("targettemperature"))
                    targetTemperature = jObject.getDouble("targettemperature");

                if (jObject.has("program"))
                    activeProgram = jObject.getInt("program");
                if (jObject.has("timerange"))
                    activeTimeRange = jObject.getInt("timerange");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public String toJson() {

        JSONObject obj = new JSONObject();

        try {
            obj.put("speed", id);
            obj.put("speed", speed);
            obj.put("avspeed", averagespeed);
            obj.put("direction", direction);
            obj.put("date", date);
            obj.put("temperature", temperature);
            obj.put("pressure", pressure);
            obj.put("humidity", humidity);
            obj.put("rainrate", rainrate);
            obj.put("sampledatetime", sampledatetime);
            obj.put("trend", trend);
            obj.put("spotid", spotID);
            int count = 0;
            for (String url : webcamurlList) {
                if (count++ == 0)
                    obj.put("webcamurl", url);
                else
                    obj.put("webcamurl"+count, url);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return obj.toString();

    }*/

}
