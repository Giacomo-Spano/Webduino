package com.webduino.scenarios;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class ScenarioTimeInterval {

    public int id;
    public int scenarioid = 0;
    public boolean enabled = false;
    public int priority = 0;
    public String name = "";
    public String description = "";
    public Date startDateTime = new Date();
    public Date endDateTime = new Date();

    public boolean sunday = false;
    public boolean monday = false;
    public boolean tuesday = false;
    public boolean wednesday = false;
    public boolean thursday = false;
    public boolean friday = false;
    public boolean saturday = false;


    public void fromJson(JSONObject json) throws JSONException {

        if (json.has("id")) id = json.getInt("id");
        if (json.has("scenarioid")) scenarioid = json.getInt("scenarioid");
        if (json.has("name")) name = json.getString("name");
        if (json.has("description")) description = json.getString("description");
        if (json.has("enabled")) enabled = json.getBoolean("enabled");
        if (json.has("priority")) priority = json.getInt("priority");
        if (json.has("startdatetime")) {
            String time = json.getString("startdatetime");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                startDateTime = df.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (json.has("enddatetime")) {
            String time = json.getString("enddatetime");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                endDateTime = df.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (json.has("sunday")) sunday = json.getBoolean("sunday");
        if (json.has("monday")) monday = json.getBoolean("monday");
        if (json.has("tuesday")) tuesday = json.getBoolean("tuesday");
        if (json.has("wednesday")) wednesday = json.getBoolean("wednesday");
        if (json.has("thursday")) thursday = json.getBoolean("thursday");
        if (json.has("friday")) friday = json.getBoolean("friday");
        if (json.has("saturday")) saturday = json.getBoolean("saturday");
    }
}
