package com.webduino.fragment.cardinfo;

import com.webduino.webduinosystems.WebduinoSystemActuator;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class WebduinoSystemActuatorCardInfo extends CardInfo {

    public WebduinoSystemActuator actuator;

    public WebduinoSystemActuatorCardInfo() {
        type = TYPE_WEBDUINOSYSTEM;
    }
}