package com.webduino.elements;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giaco on 17/02/2018.
 */

public class SensorType {

    public String type = "";
    public String description = "";
    public List<String> statusList = new ArrayList<>();
    public String valuetype = "";
    public double mindouble = .0;
    public double maxdouble = 100.00;

    public void fromJson(JSONObject json) throws Exception {

        if (json.has("value"))
            type = json.getString("value");
        if (json.has("description"))
            description = json.getString("description");
        if (json.has("statuslist")) {
            JSONArray jarray = json.getJSONArray("statuslist");
            if (jarray != null) {
                for (int i = 0; i < jarray.length(); i++) {
                    statusList.add((String) jarray.get(i));
                }
            }
            description = json.getString("description");
        }
        if (json.has("valuetype")) {
            valuetype = json.getString("valuetype");
        }
        if (valuetype.equals("double")) {
            if (json.has("valuemin")) {
                mindouble = json.getDouble("valuemin");
            }
            if (valuetype.equals("double") && json.has("valuemax")) {
                maxdouble = json.getDouble("valuemax");
            }
        }
    }
}
