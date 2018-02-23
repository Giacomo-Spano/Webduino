package com.webduino;

import com.webduino.elements.Actuator;
import com.webduino.elements.Sensor;
import com.webduino.scenarios.Scenario;
import com.webduino.zones.Zone;

import java.util.List;

/**
 * Created by giacomo on 01/07/2015.
 */
public interface AsyncRequestDataResponse {

    void processFinish(Object result, int requestType, boolean error, String errorMessage);

    void processFinishRegister(long shieldId, boolean error, String errorMessage);
    // void processFinishSensors(List<Sensor> sensors, boolean error, String errorMessage);
    //void processFinishZones(List<Zone> sensors, boolean error, String errorMessage);
    //void processFinishScenarios(List<Scenario> sensors, boolean error, String errorMessage);
    //void processFinishActuators(List<Actuator> actuators, boolean error, String errorMessage);
    void processFinishSendCommand(String response, boolean error, String errorMessage);
    //void processFinishPrograms(List<Object> programs, int requestType, boolean error, String errorMessage);
    void processFinishPostProgram(boolean response, int requestType, boolean error, String errorMessage);
    void processFinishObjectList(List<Object> list, int requestType, boolean error, String errorMessage);
}

