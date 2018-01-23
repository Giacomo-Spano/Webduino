package com.webduino.elements;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Giacomo Span� on 07/11/2015.
 */
public class NextProgramTimeRangeAction {

    public Date date;
    public Date start;
    public Date end;

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
    public String zone;


    public NextProgramTimeRangeAction() {
    }

    public void fromJson(JSONObject jsonObj) throws Exception {
        if (jsonObj.has("date")) {

            String strdate = jsonObj.getString("date");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.parse(strdate);
            Calendar datecalendar = Calendar.getInstance();
            datecalendar.setTime(date);
            datecalendar.set(Calendar.HOUR_OF_DAY,0);
            datecalendar.set(Calendar.MINUTE,0);
            datecalendar.set(Calendar.SECOND,0);
            date = datecalendar.getTime();
            SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
            if (jsonObj.has("starttime")) {
                String strstarttime = jsonObj.getString("starttime");
                start = tf.parse(strstarttime);
                Calendar time = Calendar.getInstance();
                time.setTime(start);
                time.set(Calendar.DAY_OF_MONTH,datecalendar.get(Calendar.DAY_OF_MONTH));
                time.set(Calendar.MONTH,datecalendar.get(Calendar.MONTH));
                time.set(Calendar.YEAR,datecalendar.get(Calendar.YEAR));
                start = time.getTime();
            }
            if (jsonObj.has("endtime")) {
                String strendtime = jsonObj.getString("endtime");
                end = tf.parse(strendtime);
                Calendar time = Calendar.getInstance();
                time.setTime(end);
                time.set(Calendar.DAY_OF_MONTH,datecalendar.get(Calendar.DAY_OF_MONTH));
                time.set(Calendar.MONTH,datecalendar.get(Calendar.MONTH));
                time.set(Calendar.YEAR,datecalendar.get(Calendar.YEAR));
                end = time.getTime();
            }
        }
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
            actionid = jaction.getInt("id");
            actionname = jaction.getString("name");
            zone = jaction.getString("zonename");

        }
    }

}
