package com.webduino.webduinosystems;

import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.WebduinoSystemCardInfo;
import com.webduino.zones.Zone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class WebduinoSystem {

    public int id;
    private String name;
    public List<WebduinoSystemZone> zones = new ArrayList<>();
    public List<WebduinoSystemActuator> actuators = new ArrayList<>();
    public String webduinoSystemType;

    public WebduinoSystem(JSONObject json) throws JSONException {
        fromJson(json);
    }

    public CardInfo getCardInfo() {

        WebduinoSystemCardInfo cardInfo = new WebduinoSystemCardInfo();
        cardInfo.webduionoSystemType = webduinoSystemType;
        cardInfo.id = id;
        cardInfo.name = name;
        cardInfo.webduionoSystem = this;
        cardInfo.setEnabled(true);
        return (CardInfo)cardInfo;
    }

    public void fromJson(JSONObject jObject) throws JSONException {


        if (jObject.has("id"))
            id = jObject.getInt("id");
        if (jObject.has("name"))
            name = jObject.getString("name");
        zones.clear();
        if (jObject.has("zones")) {
            JSONArray jsonArray = jObject.getJSONArray("zones");
            for (int i = 0; i < jsonArray.length(); i++) {
                WebduinoSystemZone zone = new WebduinoSystemZone(jsonArray.getJSONObject(i));
                zones.add(zone);
            }
        }
        actuators.clear();
        if (jObject.has("actuators")) {
            JSONArray jsonArray = jObject.getJSONArray("actuators");
            for (int i = 0; i < jsonArray.length(); i++) {
                WebduinoSystemActuator actuator = new WebduinoSystemActuator(jsonArray.getJSONObject(i));
                actuators.add(actuator);
            }
        }
    }
}
