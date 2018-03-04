package com.webduino.webduinosystems;

import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.WebduinoSystemCardInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class HeaterSystem extends WebduinoSystem {


    public HeaterSystem(JSONObject json, String systemtype) throws JSONException {
        super(json);
        this.webduinoSystemType = systemtype;
    }

}
