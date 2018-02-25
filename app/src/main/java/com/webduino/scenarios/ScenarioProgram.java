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

public class ScenarioProgram {

    public int id;
    public int scenarioId = 0;
    public String name = "";
    public String description = "";
    public boolean enabled = true;
    public boolean sunday = true;
    public boolean monday = true;
    public boolean tuesday = true;
    public boolean wednesday = true;
    public boolean thursday = true;
    public boolean friday = true;
    public boolean saturday = true;
    public int priority = 0;

    private boolean active = false;

    private Date nextProgramTimeRangeJobDate = null;
    private Date startDate = null;
    private Date endDate = null;
    private Date programLastEndDate = null;

    public List<ScenarioProgramTimeRange> timeRanges = new ArrayList<>();

    public void fromJson(JSONObject json) throws Exception {

        if (json.has("id")) id = json.getInt("id");
        if (json.has("scenarioid")) scenarioId = json.getInt("scenarioid");
        if (json.has("name")) name = json.getString("name");
        if (json.has("description")) description = json.getString("description");
        if (json.has("enabled")) enabled = json.getBoolean("enabled");
        if (json.has("sunday")) sunday = json.getBoolean("sunday");
        if (json.has("monday")) monday = json.getBoolean("monday");
        if (json.has("tuesday")) tuesday = json.getBoolean("tuesday");
        if (json.has("wednesday")) wednesday = json.getBoolean("wednesday");
        if (json.has("thursday")) thursday = json.getBoolean("thursday");
        if (json.has("friday")) friday = json.getBoolean("friday");
        if (json.has("saturday")) saturday = json.getBoolean("saturday");
        if (json.has("priority")) priority = json.getInt("priority");
        if (json.has("timeranges")) {
            JSONArray jsonArray = json.getJSONArray("timeranges");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                ScenarioProgramTimeRange timeRange = new ScenarioProgramTimeRange(jo);
                if (timeRange != null) {

                    if (i > 0 && timeRanges.get(timeRanges.size() - 1).endTime.compareTo(timeRange.startTime) > 0)
                        throw new Exception("time range " + i + "cannot start before time range " + (i - 1));
                    timeRanges.add(timeRange);
                }
            }
        }
    }

    public JSONObject toJson() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("id", id);
        json.put("scenarioid", scenarioId);
        json.put("name", name);
        json.put("description", description);
        json.put("enabled", enabled);
        json.put("sunday", sunday);
        json.put("monday", monday);
        json.put("tuesday", tuesday);
        json.put("wednesday", wednesday);
        json.put("thursday", thursday);
        json.put("friday", friday);
        json.put("saturday", saturday);
        json.put("priority", priority);
        JSONArray jarray = new JSONArray();
        if (timeRanges != null) {
            for (ScenarioProgramTimeRange timeRange : timeRanges) {
                jarray.put(timeRange.toJson());
            }
            json.put("timeranges", jarray);
        }

        return json;

    }


    public ScenarioProgramTimeRange getTimeRangeFromId(int id) {
        for (ScenarioProgramTimeRange timeRange : timeRanges) {
            if (timeRange.id == id)
                return timeRange;
        }
        return null;
    }
}
