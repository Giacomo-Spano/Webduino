package com.webduino.fragment;

import com.webduino.AsyncRequestDataResponse;
import com.webduino.HeaterActivity;
import com.webduino.MainActivity;
import com.webduino.elements.Actuator;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.HeaterListItem;
import com.webduino.fragment.adapters.HeaterListListener;
import com.webduino.scenarios.Scenario;
import com.webduino.zones.Zone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giaco on 07/01/2018.
 */

public class HeaterNextActionsPageFragment extends PageFragment {


    public static PageFragment newInstance() {
        PageFragment fragment = new HeaterNextActionsPageFragment();
        return fragment;
    }

    @Override
    public void refreshData() {

        HeaterActivity a = (HeaterActivity) getActivity();
        int id = a.getSensorId();
        HeaterActuator heater = (HeaterActuator) Sensors.getFromId(id);


        new requestDataTask(MainActivity.activity, new AsyncRequestDataResponse() {
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
        }, requestDataTask.REQUEST_ACTUATORPROGRAMTIMERANGEACTITONS).execute(id);



        if (heater == null)
            return;

        if (!adaptercreated)
            return;

        list = new ArrayList<>();
        int count = 0;

        // Heater
        HeaterListItem mi = new HeaterListItem();
        mi.type = 0;
        mi.description = "Stato";
        list.add(mi);
        // id
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Id";
        mi.value = "" + heater.getId();
        list.add(mi);
        // shieldid
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "ShieldId";
        mi.value = "" + heater.getShieldId();
        list.add(mi);
        // name
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Nome";
        mi.value = "" + heater.getName();
        list.add(mi);
        // stato
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Stato";
        mi.value = "" + heater.getStatus();
        list.add(mi);
        // relestatus
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Relè";
        if (heater.getReleStatus())
            mi.value = "ACCESO";
        else
            mi.value = "SPENTO";
        list.add(mi);
        // temperature
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Temperatura";
        mi.value = "" + heater.getRemoteTemperature() + "°C " + heater.getLastTemperatureUpdate();
        list.add(mi);
        // zone
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Zona";
        mi.value = "" + heater.getZoneId();
        list.add(mi);
        // target
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Target";
        mi.value = "" + heater.getTarget() + "°C";
        list.add(mi);
        // last command
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Ultimo comando";
        mi.value = heater.getLastCommandDate();
        list.add(mi);

        // Heater
        mi = new HeaterListItem();
        mi.type = 0;
        mi.description = "Programma";
        list.add(mi);
        // action
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Action";
        mi.value = "" /*+ /*heater.get*/;
        list.add(mi);
        // action
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Fine programma";
        mi.value = heater.getEndDate();
        list.add(mi);
        // action
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Durata";
        mi.value = heater.getDuration();
        list.add(mi);
        // action
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Tempo rimanente";
        mi.value = heater.getRemainig();
        list.add(mi);

        HeaterListListener.HeaterListArrayAdapter adapter = new HeaterListListener.HeaterListArrayAdapter(getActivity(), list, listener);
        listView.setAdapter(adapter);
    }
}
