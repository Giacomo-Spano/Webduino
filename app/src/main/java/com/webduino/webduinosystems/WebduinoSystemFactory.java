package com.webduino.webduinosystems;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by giaco on 29/04/2017.
 */

public class WebduinoSystemFactory {

    public static final String HEATERSYSTEM = "heatersystem";
    public static final String SECURITYSYSTEM = "securitysystem";

    static public WebduinoSystem createWebduinoSystem(JSONObject json) throws JSONException {

        if (!json.has("type"))
           return null;
        String type = json.getString("type");
        try {
            WebduinoSystem webduinoSystem = null;
            if (type.equals(HEATERSYSTEM)) {
                webduinoSystem = (WebduinoSystem) new HeaterSystem(json,type);
            } else if (type.equals(SECURITYSYSTEM)) {
                webduinoSystem = (WebduinoSystem) new SecuritySystem(json,type);
            }
            return webduinoSystem;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
