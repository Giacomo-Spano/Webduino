package com.webduino;

import java.util.List;

/**
 * Created by giacomo on 01/07/2015.
 */
public interface AsyncRequestDataResponse {

    void processFinishRegister(long shieldId, boolean error, String errorMessage);
    void processFinishSensors(List<Sensor> sensors, boolean error, String errorMessage);
    void processFinishActuators(List<Actuator> actuators, boolean error, String errorMessage);
    /*void processFinishHistory(long spotId, List<ShieldData> list, boolean error, String errorMessage);
    void processFinishAddFavorite(long spotId, boolean error, String errorMessage);
    void processFinishRemoveFavorite(long spotId, boolean error, String errorMessage);*/


}