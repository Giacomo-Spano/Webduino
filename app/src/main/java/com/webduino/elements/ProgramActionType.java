package com.webduino.elements;

import org.json.JSONObject;

/**
 * Created by giaco on 17/02/2018.
 */

public class ProgramActionType {

    public String instruction = "";
    public String description = "";

    public void fromJson(JSONObject json) throws Exception {

        if (json.has("instruction"))
            instruction = json.getString("instruction");
        if (json.has("description"))
            description = json.getString("description");

    }
}
