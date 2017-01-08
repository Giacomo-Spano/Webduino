package com.webduino;

import com.webduino.elements.Actuator;
import com.webduino.elements.Program;
import com.webduino.elements.Sensor;

import java.util.List;

/**
 * Created by giaco on 08/01/2017.
 */

public class WebduinoResponse implements AsyncRequestDataResponse {
    @Override
    public void processFinishRegister(long shieldId, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishSensors(List<Sensor> sensors, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishActuators(List<Actuator> actuators, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishSendCommand(Actuator actuator, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishPrograms(List<Program> programs, int requestType, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishPostProgram(boolean response, int requestType, boolean error, String errorMessage) {

    }
}
