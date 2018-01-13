package com.webduino.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Giacomo Span� on 07/11/2015.
 */
public class NextProgramTimeRangeAction {

    public String date;
    public String start;
    public String end;

    public int actionid;
    public String actionname;
    public int actiontype;



    public int timerangeid;
    public String timerangename;
    public int programid;
    public String programname;
    public int scenarioid;
    public String scenarioname;
    public int timeintervalid;
    public int actuatorid;
    public String conflicts;
    private double target;


    public NextProgramTimeRangeAction() {
    }

    public void fromJson(JSONObject jsonObj) {

        try {
            if (jsonObj.has("start"))
                start = jsonObj.getString("start");
            if (jsonObj.has("end"))
                end = jsonObj.getString("end");
            if (jsonObj.has("date"))
                date = jsonObj.getString("date");
            if (jsonObj.has("action")) {
                JSONObject jaction = new JSONObject(jsonObj.getString("action"));
                target = jaction.getDouble("targetvalue");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
