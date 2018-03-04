package com.webduino.fragment.cardinfo;

import com.webduino.fragment.ScenarioFragment;
import com.webduino.webduinosystems.WebduinoSystem;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class WebduinoSystemCardInfo extends CardInfo {



    public String zone = "";
    public String webduionoSystemType;
    public WebduinoSystem webduionoSystem;

    public WebduinoSystemCardInfo() {
        type = TYPE_WEBDUINOSYSTEM;
    }
}