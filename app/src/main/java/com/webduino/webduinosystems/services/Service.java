package com.webduino.webduinosystems.services;

import com.webduino.ActionCommand;
import com.webduino.zones.ZoneSensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class Service {

    public int id;
    public String name;
    private String type;
    public List<ZoneSensor> zoneSensors = new ArrayList<>();
    public List<ActionCommand> actioncommandlist = new ArrayList<>();

    private double temperature = 0.0;
    private boolean doorStatusOpen = false;
    public Date lastTemperatureUpdate = null;

    public Service(JSONObject json) throws JSONException {
        fromJson(json);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    void fromJson(JSONObject json) throws JSONException {

        if (json.has("id"))
            id = json.getInt("id");
        if (json.has("name"))
            name = json.getString("name");
        if (json.has("type"))
            type = json.getString("type");

        if (json.has("actioncommandlist")) {
            JSONArray jsonArray = json.getJSONArray("actioncommandlist");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ActionCommand actioncommand = new ActionCommand(jsonObject);
                    actioncommandlist.add(actioncommand);
                }
            }
        }
    }
}
