package com.webduino.elements;


import org.json.JSONObject;

public class PressureSensor extends Sensor {

    private boolean statusOpen;

    public PressureSensor(JSONObject json) {
        super(json);
    }

    public boolean getOpenStatus() {
        return statusOpen;
    }

    @Override
    void fromJson(JSONObject json) {

        super.fromJson(json);

        try {
            if (json.has("open"))
                statusOpen = json.getBoolean("open");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
