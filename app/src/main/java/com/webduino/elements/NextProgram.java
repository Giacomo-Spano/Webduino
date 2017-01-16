package com.webduino.elements;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Giacomo Span� on 07/11/2015.
 */
public class NextProgram {
    public int id;
    public String name;
    public double temperature;
    public int sensorId;
    public Date startDate;
    public Date endDate;
    public int timeRangeId;
    public String timeRangeName;

    public NextProgram() {
    }

    public void fromJson(JSONObject jsonObj) {

        try {
            if (jsonObj.has("id"))
                id = jsonObj.getInt("id");
            if (jsonObj.has("name"))
                name = jsonObj.getString("name");
            if (jsonObj.has("temperature"))
                temperature = jsonObj.getDouble("temperature");
            if (jsonObj.has("sensor"))
                sensorId = jsonObj.getInt("sensor");
            if (jsonObj.has("timerangeid"))
                timeRangeId = jsonObj.getInt("timerangeid");
            if (jsonObj.has("timerangename"))
                timeRangeName = jsonObj.getString("timerangename");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            if (jsonObj.has("startdate")) {
                String dateInString = jsonObj.getString("startdate");
                try {
                    startDate = formatter.parse(dateInString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (jsonObj.has("enddate")) {
                String dateInString = jsonObj.getString("enddate");
                try {
                    endDate = formatter.parse(dateInString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
