package com.webduino;

import com.webduino.elements.Actuator;
import com.webduino.elements.Program;
import com.webduino.elements.Sensor;

import java.util.List;

/**
 * Created by giacomo on 01/07/2015.
 */
public interface AsyncRequestDataResponse {

    void processFinishRegister(long shieldId, boolean error, String errorMessage);
    void processFinishSensors(List<Sensor> sensors, boolean error, String errorMessage);
    void processFinishActuators(List<Actuator> actuators, boolean error, String errorMessage);
    void processFinishSendCommand(Actuator actuator, boolean error, String errorMessage);
    void processFinishPrograms(List<Object> programs, int requestType, boolean error, String errorMessage);
    void processFinishPostProgram(boolean response, int requestType, boolean error, String errorMessage);
    void processFinishObjectList(List<Object> list, int requestType, boolean error, String errorMessage);
}