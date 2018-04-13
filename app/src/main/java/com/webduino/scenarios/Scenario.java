package com.webduino.scenarios;

import com.webduino.MainActivity;
import com.webduino.WebduinoResponse;
import com.webduino.elements.Program;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ScenarioCardInfo;
import com.webduino.webduinosystems.WebduinoSystem;
import com.webduino.webduinosystems.WebduinoSystems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by giaco on 16/12/2017.
 */

public class Scenario {

    public int id;
    public int webduinosystemid;
    public boolean enabled = false;
    public String name = "";
    public String description = "";
    public Date startDate, endDate;
    public ScenarioCalendar calendar = new ScenarioCalendar();
    public List<ScenarioTrigger> triggers = new ArrayList<>();
    public List<ScenarioProgram> programs = new ArrayList<>();
    public int priority = 0;

    public String status = "";

    public Scenario(int webduinosystemid) {
        this.webduinosystemid = webduinosystemid;
    }

    public Scenario(JSONObject jObject) throws Exception {
        fromJson(jObject);
    }

    public void fromJson(JSONObject jObject) throws Exception {

        try {
            if (jObject.has("id"))
                id = jObject.getInt("id");
            if (jObject.has("webduinosystemid")) {
                webduinosystemid = jObject.getInt("webduinosystemid");
            }
            if (jObject.has("name"))
                name = jObject.getString("name");
            if (jObject.has("description"))
                description = jObject.getString("description");

            if (jObject.has("startdate")) {
                String time = jObject.getString("startdate");
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                try {
                    startDate = df.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (jObject.has("enddate")) {
                String time = jObject.getString("enddate");
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                try {
                    endDate = df.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (jObject.has("calendar")) {
                JSONObject calendarjson = jObject.getJSONObject("calendar");
                if (calendarjson.has("timeintervals")) {
                    JSONArray jsonArray = calendarjson.getJSONArray("timeintervals");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ScenarioTimeInterval timeinterval = new ScenarioTimeInterval();

                        timeinterval.fromJson(jsonArray.getJSONObject(i));
                        calendar.timeIntervals.add(timeinterval);
                    }
                }
            }

            if (jObject.has("triggers")) {
                JSONArray jsonArray = jObject.getJSONArray("triggers");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ScenarioTrigger trigger = new ScenarioTrigger();
                    trigger.fromJson(jsonArray.getJSONObject(i));
                    triggers.add(trigger);
                }
            }

            if (jObject.has("programs")) {
                JSONArray jsonArray = jObject.getJSONArray("programs");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ScenarioProgram program = new ScenarioProgram();
                    try {
                        program.fromJson(jsonArray.getJSONObject(i));
                        programs.add(program);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (jObject.has("priority"))
                priority = jObject.getInt("priority");
            if (jObject.has("enabled"))
                enabled = jObject.getBoolean("enabled");

            if (jObject.has("status"))
                status = jObject.getString("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public CardInfo getCardInfo() {

        CardInfo cardInfo = new ScenarioCardInfo();
        cardInfo.id = id;
        ((ScenarioCardInfo)cardInfo).scenario = this;
        cardInfo.name = name;
        cardInfo.enabled = enabled;
        cardInfo.label = "Non attivo";

        cardInfo.online = false;
        cardInfo.setEnabled(true);

        return cardInfo;
    }

    public ScenarioTrigger getTriggerFromId(int id) {
        for (ScenarioTrigger trigger:triggers) {
            if (trigger.id == id)
                return trigger;
        }
        return null;
    }

    public ScenarioProgram getProgramFromId(int id) {
        for (ScenarioProgram program:programs) {
            if (program.id == id)
                return program;
        }
        return null;
    }

    public JSONObject toJson() {

        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("webduinosystemid", webduinosystemid);
            json.put("name", name);
            json.put("description", description);
            //json.put("dateenabled", dateEnabled);
            json.put("enabled", enabled);
            json.put("calendar", calendar.toJson());
            json.put("priority", priority);
            JSONArray jarray = new JSONArray();
            if (triggers != null) {
                for (ScenarioTrigger trigger : triggers) {
                    jarray.put(trigger.toJson());
                }
                json.put("triggers", jarray);
            }
            jarray = new JSONArray();
            if (programs != null) {
                for (ScenarioProgram program : programs) {
                    jarray.put(program.toJson());
                }
                json.put("programs", jarray);
            }
            json.put("priority", priority);
            // dynamic values

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public ScenarioProgram getProgramFromTimerangeId(int id) {
        for (ScenarioProgram program:programs) {
            for (ScenarioProgramTimeRange timeRange: program.timeRanges) {
                if (timeRange.id == id)
                    return program;
            }
        }
        return null;
    }
}
