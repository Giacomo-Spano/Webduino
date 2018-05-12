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
    public List<Condition> conditions = new ArrayList<>();
    public List<Action> actions = new ArrayList<>();

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
        } else {
            throw new Exception("missing start/end time");
        }
        if (json.has("enabled"))
            enabled = json.getBoolean("enabled");
        if (json.has("index"))
            index = json.getInt("index");


        if (json.has("conditions")) {
            JSONArray jsonArray = json.getJSONArray("conditions");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonCondition = jsonArray.getJSONObject(i);
                    Condition condition = new Condition(jsonCondition);
                    if (condition != null)
                        conditions.add(condition);
                }
            }
        }
        if (json.has("actions")) {
            JSONArray jsonArray = json.getJSONArray("actions");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonAction = jsonArray.getJSONObject(i);
                    Action action = new Action(jsonAction);
                    if (action != null)
                        actions.add(action);
                }
            }
        }
    }

    /*public ProgramInstruction getActionFromId(int id) {
        for (ProgramInstruction action : programInstructionList) {
            if (action.id == id)
                return action;
        }
        return null;
    }*/

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

        JSONArray jarrayconditions = new JSONArray();
        if (conditions != null) {
            for (Condition condition : conditions) {
                jarrayconditions.put(condition.toJson());
            }
            json.put("conditions", jarrayconditions);
        }
        JSONArray jarrayactions = new JSONArray();
        if (actions != null) {
            for (Action action : actions) {
                jarrayactions.put(action.toJson());
            }
            json.put("actions", jarrayactions);
        }

        return json;
    }

    /*public ProgramInstruction getProgramActionFromId(int id) {
        for (ProgramInstruction action : programInstructionList) {
            if (action.id == id)
                return action;
        }
        return null;
    }*/
}
