package com.webduino.elements;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Giacomo Span� on 07/11/2015.
 */
public class Program {
    public int id;
    public boolean active;
    public boolean dateEnabled;
    public String name;
    public boolean Sunday;
    public boolean Monday;
    public boolean Tuesday;
    public boolean Wednesday;
    public boolean Thursday;
    public boolean Friday;
    public boolean Saturday;
    public Date startDate;
    public Date endDate;
    public Time startTime;
    public Time endTime;
    public List<TimeRange> timeRanges = new ArrayList<TimeRange>();
    public int priority;

    public Program() {
        name = "Nuovo programma";
        active = true;
        dateEnabled = false;
        startDate = new Date();
        endDate = new Date();
        Sunday = false;
        Monday = false;
        Tuesday = false;
        Wednesday = false;
        Thursday = false;
        Friday = false;
        Saturday = false;
        TimeRange tr = new TimeRange();
        tr.starTime = Time.valueOf("00:00:00");
        tr.endTime = Time.valueOf("23:59:00");
        tr.temperature = 15.0;
        tr.name = "fascia1";
        timeRanges.add(tr);
    }

    TimeRange getTimeRangeFromId(int id) {

        Iterator<TimeRange> iterator = timeRanges.iterator();
        TimeRange currentTimeRange = null;
        while (iterator.hasNext()) {

            currentTimeRange = iterator.next();

            if (currentTimeRange.ID == id) {

                return currentTimeRange;
            }
        }
        return null;
    }

    public void fromJson(JSONObject jsonObj) {

        try {
            if (jsonObj.has("id"))
                id = jsonObj.getInt("id");
            if (jsonObj.has("name"))
                name = jsonObj.getString("name");
            if (jsonObj.has("active"))
                active = jsonObj.getBoolean("active");
            if (jsonObj.has("dateenabled"))
                dateEnabled = jsonObj.getBoolean("dateenabled");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

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

            if (jsonObj.has("starttime")) {
                String timeInString = jsonObj.getString("starttime") + ":00";
                startTime = Time.valueOf(timeInString);
            }
            if (jsonObj.has("endtime")) {
                String timeInString = jsonObj.getString("endtime") + ":00";
                endTime = Time.valueOf(timeInString);
            }

            Sunday = jsonObj.getBoolean("sunday");
            Monday = jsonObj.getBoolean("monday");
            Tuesday = jsonObj.getBoolean("tuesday");
            Wednesday = jsonObj.getBoolean("wednesday");
            Thursday = jsonObj.getBoolean("thursday");
            Friday = jsonObj.getBoolean("friday");
            Saturday = jsonObj.getBoolean("saturday");

            Time lastTime = Time.valueOf("00:0:00");
            JSONArray timeranges = jsonObj.getJSONArray("timeranges");
            for (int i = 0; i < timeranges.length(); i++) {
                JSONObject JSONrange = timeranges.getJSONObject(i);

                TimeRange tr = new TimeRange();
                if (!JSONrange.isNull(("id")))
                    tr.ID = JSONrange.getInt("id");
                if (!JSONrange.isNull(("name")))
                    tr.name = JSONrange.getString("name");
                if (!JSONrange.isNull(("endtime")))
                    tr.endTime = Time.valueOf(JSONrange.getString("endtime") + ":00");
                tr.starTime = lastTime;
                lastTime = tr.endTime;
                if (!JSONrange.isNull(("temperature")))
                    tr.temperature = JSONrange.getDouble("temperature");
                if (!JSONrange.isNull(("sensorid")))
                    tr.sensorId = JSONrange.getInt("sensorid");
                if (!JSONrange.isNull(("priority")))
                    tr.priority = JSONrange.getInt("priority");

                timeRanges.add(tr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJson() {

        JSONObject json = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            json.put("id", id);
            json.put("active", active);
            json.put("name", name);
            json.put("dateenabled", dateEnabled);
            json.put("startdate", startDate);
            if (startTime != null)
                json.put("starttime", df.format(startTime));
            json.put("enddate", endDate);
            if (endTime != null)
                json.put("endtime", df.format(endTime));
            json.put("sunday", Sunday);
            json.put("monday", Monday);
            json.put("tuesday", Tuesday);
            json.put("wednesday", Wednesday);
            json.put("thursday", Thursday);
            json.put("friday", Friday);
            json.put("saturday", Saturday);
            json.put("priority", priority);

            JSONArray timerange = new JSONArray();
            for(TimeRange tr : timeRanges) {
                JSONObject range = new JSONObject();
                range.put("id", tr.ID);
                range.put("name", tr.name);
                if (tr.endTime != null)
                    range.put("endtime", df.format(tr.endTime));
                range.put("sensorid", tr.sensorId);
                //range.put("subaddress", tr.subAddress);
                range.put("temperature", tr.temperature);
                range.put("priority", tr.priority);
                timerange.put(range);
            }
            json.put("timeranges", timerange);
            return json;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
