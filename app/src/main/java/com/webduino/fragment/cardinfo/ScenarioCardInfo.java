package com.webduino.fragment.cardinfo;

import com.webduino.elements.HeaterActuator;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class ScenarioCardInfo extends CardInfo {


    public String zone = "";


    public ScenarioCardInfo() {
        type = TYPE_SCENARIO;
        //online = true; // sempre online altrimentio diventa grigio
    }
}