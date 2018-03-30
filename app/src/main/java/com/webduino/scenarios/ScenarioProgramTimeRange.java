package com.webduino.scenarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class ScenarioProgramTimeRange {
    boolean active = false;

    public int id;
    public int programid;
    public String name;
    public String description;
    public Date startTime = new Date();
    public Date endTime = new Date();
    public boolean enabled;
    public int index;

    public List<ProgramAction> programActionList = new ArrayList<>();

    public ScenarioProgramTimeRange() {
    }

    public ScenarioProgramTimeRange(JSONObject json) throws Exception {
        fromJson(json);
    }

    public void fromJson(JSONObject json) throws Exception {

        if (json.has("id"))
            id = json.getInt("id");
        if (json.has("programid"))
            programid = json.getInt("programid");
        if (json.has("name"))
            name = json.getString("name");
        if (json.has("description"))
            description = json.getString("description");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        if (json.has("starttime") && json.has("endtime")) {
            String str = json.getString("starttime");
            startTime = df.parse(str);
            str = json.getString("endtime");
            endTime = df.parse(str);
            /*if (endTime.compareTo(startTime) < 0)
                throw new Exception("start time " + startTime.toString() + "must be before end time " + endTime.toString());*/
        } else {
            throw new Exception("missing start/end time");
        }
        if (json.has("enabled"))
            enabled = json.getBoolean("enabled");
        if (json.has("index"))
            index = json.getInt("index");

        if (json.has("actions")) {

            ProgramActionFactory factory = new ProgramActionFactory();
            JSONArray jsonArray = json.getJSONArray("actions");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                ProgramAction action = null;
                try {
                    action = factory.fromJson(jo);
                    programActionList.add(action);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception(e.toString());
                }
            }
        }
    }

    public ProgramAction getActionFromId(int id) {
        for (ProgramAction action : programActionList) {
            if (action.id == id)
                return action;
        }
        return null;
    }

    public JSONObject toJson() throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("programid", programid);
        json.put("name", name);
        json.put("description", description);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        json.put("starttime", df.format(startTime));
        json.put("endtime", df.format(endTime));
        json.put("enabled", enabled);
        json.put("index", index);

        JSONArray jarray = new JSONArray();
        if (programActionList != null) {
            for (ProgramAction action : programActionList) {
                jarray.put(action.toJson());
            }
            json.put("actions", jarray);
        }

        return json;
    }

    public ProgramAction getProgramActionFromId(int id) {
        for (ProgramAction action : programActionList) {
            if (action.id == id)
                return action;
        }
        return null;
    }
}
