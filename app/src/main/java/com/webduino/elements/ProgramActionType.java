package com.webduino.elements;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giaco on 17/02/2018.
 */

public class ProgramActionType {

    public String instruction = "";
    public String description = "";
    public List<String> statusList = new ArrayList<>();

    public void fromJson(JSONObject json) throws Exception {

        if (json.has("instruction"))
            instruction = json.getString("instruction");
        if (json.has("description"))
            description = json.getString("description");
    }
}
