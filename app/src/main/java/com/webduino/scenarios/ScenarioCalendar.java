package com.webduino.scenarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class ScenarioCalendar {
    public List<ScenarioTimeInterval> timeIntervals = new ArrayList<>();
    public int priority;


    public ScenarioTimeInterval getTimeIntervalFromId(int id) {
        for (ScenarioTimeInterval timeInterval : timeIntervals) {
            if (timeInterval.id == id)
                return timeInterval;
        }
        return null;
    }

    protected JSONObject toJson() throws JSONException {

        JSONObject json = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        //json.put("dateenabled", dateEnabled);
        json.put("priority", priority);
        JSONArray timeIntervalsJArray = new JSONArray();
        for (ScenarioTimeInterval timeInterval : timeIntervals) {
            JSONObject JSONInterval = timeInterval.toJson();
            timeIntervalsJArray.put(JSONInterval);
        }
        json.put("timeintervals", timeIntervalsJArray);

        return json;
    }
}
