package com.webduino;

import com.webduino.elements.CurrentSensor;
import com.webduino.elements.DoorSensor;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.HornSensor;
import com.webduino.elements.HumiditySensor;
import com.webduino.elements.OnewireSensor;
import com.webduino.elements.PIRSensor;
import com.webduino.elements.PressureSensor;
import com.webduino.elements.Sensor;
import com.webduino.elements.TemperatureSensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by giaco on 29/04/2017.
 */

public class SensorFactory {

    static public Sensor createSensor(JSONObject json) {

        if (!json.has("type"))
            return null;
        String type = null;
        try {
            type = json.getString("type");

            Sensor sensor;
            if (type.equals("temperaturesensor")) {
                sensor = (Sensor) new TemperatureSensor(json);
            } /*else if (type.equals("onewiresensor")) {
                sensor = (Sensor) new OnewireSensor(json);
            } */else if (type.equals("currentsensor")) {
                sensor = (Sensor) new CurrentSensor(json);
            } else if (type.equals("doorsensor")) {
                sensor = (Sensor) new DoorSensor(json);
            } else if (type.equals("onewiresensor")) {
                sensor = (Sensor) new OnewireSensor(json);
            } else if (type.equals("humiditysensor")) {
                sensor = (Sensor) new HumiditySensor(json);
            } else if (type.equals("pirsensor")) {
                sensor = (Sensor) new PIRSensor(json);
            } else if (type.equals("pressuresensor")) {
                sensor = (Sensor) new PressureSensor(json);
            } else if (type.equals("heatersensor")) {
                sensor = (Sensor) new HeaterActuator(json);
            } else if (type.equals("hornsensor")) {
                sensor = (Sensor) new HornSensor(json);
            } else {
                return null;
            }

            if (json.has("childsensors")) {
                JSONArray jArray = json.getJSONArray("childsensors");
                for(int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonChild = jArray.getJSONObject(i);
                    Sensor child = createSensor(jsonChild);
                    sensor.childsensors.add(child);
                }
            }

            return sensor;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
