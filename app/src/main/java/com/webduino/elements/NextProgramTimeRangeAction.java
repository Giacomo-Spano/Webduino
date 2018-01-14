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
    public String actiontype;



    public int timerangeid;
    public String timerangename;
    public int programid;
    public String programname;
    public int scenarioid;
    public String scenarioname;
    public int timeintervalid;
    public int actuatorid;
    public String conflicts;
    public double target;


    public NextProgramTimeRangeAction() {
    }

    public void fromJson(JSONObject jsonObj) {

        try {
            if (jsonObj.has("starttime"))
                start = jsonObj.getString("starttime");
            if (jsonObj.has("endtime"))
                end = jsonObj.getString("endtime");
            if (jsonObj.has("date"))
                date = jsonObj.getString("date");
            if (jsonObj.has("scenarioid"))
                scenarioid = jsonObj.getInt("scenarioid");
            if (jsonObj.has("scenarioname"))
                scenarioname = jsonObj.getString("scenarioname");
            if (jsonObj.has("programid"))
                programid = jsonObj.getInt("programid");
            if (jsonObj.has("scenarioname"))
                programname = jsonObj.getString("programname");
            if (jsonObj.has("timerangeid"))
                timerangeid = jsonObj.getInt("timerangeid");
            if (jsonObj.has("timerangename"))
                timerangename = jsonObj.getString("timerangename");
            if (jsonObj.has("timeintervalid"))
                timeintervalid = jsonObj.getInt("timeintervalid");
            if (jsonObj.has("action")) {
                JSONObject jaction = new JSONObject(jsonObj.getString("action"));
                target = jaction.getDouble("targetvalue");
                actiontype = jaction.getString("type");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
