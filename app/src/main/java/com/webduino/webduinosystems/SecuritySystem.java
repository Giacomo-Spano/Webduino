package com.webduino.webduinosystems;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class SecuritySystem extends WebduinoSystem {

    public SecuritySystem(JSONObject json, String systemtype) throws Exception {
        super(json);
        this.webduinoSystemType = systemtype;
    }
}
