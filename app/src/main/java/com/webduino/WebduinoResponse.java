package com.webduino;

import com.webduino.elements.Actuator;
import com.webduino.elements.Sensor;
import com.webduino.scenarios.Scenario;
import com.webduino.zones.Zone;

import java.util.List;

/**
 * Created by giaco on 08/01/2017.
 */

public class WebduinoResponse implements AsyncRequestDataResponse {
    @Override
    public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishRegister(long shieldId, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishSendCommand(String response, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishPostProgram(boolean response, int requestType, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishObjectList(List<Object> list, int requestType, boolean error, String errorMessage) {

    }
}
