package com.webduino;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionCommand {
    public String command;
    public String name;
    public String paramType;
    public String paramDescription;

    public ActionCommand(JSONObject json) throws JSONException {
        fromJson(json);
    }

    void fromJson(JSONObject json) throws JSONException {
        if (json.has("command"))
            command = json.getString("command");
        if (json.has("name"))
            name = json.getString("name");
        if (json.has("paramType"))
            paramType = json.getString("paramType");
        if (json.has("paramDescription"))
            paramType = json.getString("paramDescription");
    }

}
