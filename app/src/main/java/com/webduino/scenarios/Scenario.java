package com.webduino.scenarios;

import android.app.Fragment;

import com.webduino.fragment.ScenariosFragment;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ScenarioCardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;

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
    public boolean active = false;
    public String name = "";
    public String description = "";
    public ScenarioCalendar calendar = new ScenarioCalendar();
    public List<ScenarioTrigger> triggers = new ArrayList<>();
    public List<ScenarioProgram> programs = new ArrayList<>();
    public int priority = 0;
    //public boolean dateEnabled = true;
    public boolean enabled = false;


    public void fromJson(JSONObject jObject) {

        try {
            if (jObject.has("id"))
                id = jObject.getInt("id");
            if (jObject.has("name"))
                name = jObject.getString("name");
            if (jObject.has("description"))
                description = jObject.getString("description");

            if (jObject.has("calendar")) {
                JSONObject calendarjson = jObject.getJSONObject("calendar");
                if (calendarjson.has("timeintervals")) {
                    JSONArray jsonArray = calendarjson.getJSONArray("timeintervals");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ScenarioTimeInterval timeinterval = new ScenarioTimeInterval();

                        timeinterval.fromJson(jsonArray.getJSONObject(i));
                        calendar.timeintervals.add(timeinterval);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public CardInfo getCardInfo(Fragment context) {
        return getCardInfo(context, null);
    }

    public CardInfo getCardInfo(Fragment context, CardInfo cardInfo) {

        if (cardInfo == null)
            cardInfo = new ScenarioCardInfo();
        cardInfo.id = id;
        cardInfo.name = name;
        cardInfo.online = false;
        cardInfo.setEnabled(true);

        return cardInfo;
    }
}
