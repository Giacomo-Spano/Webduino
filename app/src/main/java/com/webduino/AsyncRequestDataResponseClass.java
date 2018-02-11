package com.webduino;

import com.webduino.elements.Actuator;
import com.webduino.elements.Sensor;
import com.webduino.scenarios.Scenario;
import com.webduino.zones.Zone;

import java.util.List;

public abstract class AsyncRequestDataResponseClass implements AsyncRequestDataResponse{
    @Override
    public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishRegister(long shieldId, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishSensors(List<Sensor> sensors, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishZones(List<Zone> sensors, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishScenarios(List<Scenario> sensors, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishActuators(List<Actuator> actuators, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishSendCommand(String response, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishPrograms(List<Object> programs, int requestType, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishPostProgram(boolean response, int requestType, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishObjectList(List<Object> list, int requestType, boolean error, String errorMessage) {

    }
}
