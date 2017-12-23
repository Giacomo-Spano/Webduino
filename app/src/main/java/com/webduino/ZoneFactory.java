package com.webduino;

import com.webduino.elements.CurrentSensor;
import com.webduino.elements.DoorSensor;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.HumiditySensor;
import com.webduino.elements.OnewireSensor;
import com.webduino.elements.PIRSensor;
import com.webduino.elements.PressureSensor;
import com.webduino.elements.Sensor;
import com.webduino.elements.TemperatureSensor;
import com.webduino.zones.Zone;
import com.webduino.zones.ZoneSensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by giaco on 29/04/2017.
 */

public class ZoneFactory {

    static public Zone createZone(JSONObject json) {

        /*if (!json.has("type"))
            return null;*/
        String type = "";
        try {
            //type = json.getString("type");

            Zone zone;
            if (type.equals("heaterzone")) {
                zone = (Zone) new Zone(json);
            } else if (type.equals("securityzone")) {
                zone = (Zone) new Zone(json);
            } else {
                zone = (Zone) new Zone(json);
            }

            if (json.has("zonesensors")) {
                JSONArray jArray = json.getJSONArray("zonesensors");
                for(int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonSensor = jArray.getJSONObject(i);
                    ZoneSensor zoneSensor = new ZoneSensor(jsonSensor);
                    zone.zoneSensors.add(zoneSensor);
                }
            }

            return zone;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
