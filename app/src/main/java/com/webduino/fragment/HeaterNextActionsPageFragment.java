package com.webduino.fragment;

import android.app.Activity;

import com.webduino.AsyncRequestDataResponse;
import com.webduino.HeaterActivity;
import com.webduino.MainActivity;
import com.webduino.elements.Actuator;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.NextProgramTimeRangeAction;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.HeaterListListener;
import com.webduino.fragment.adapters.HeaterNextActionRowItem;
import com.webduino.fragment.adapters.ListItem;
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

        if (heater == null)
            return;

        if (!adaptercreated)
            return;

        final Activity activity = getActivity();


        new requestDataTask(MainActivity.activity, new AsyncRequestDataResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                List<NextProgramTimeRangeAction> nextActionList = (List<NextProgramTimeRangeAction>) result;

                list = new ArrayList<>();
                //int count = 0;
                for (NextProgramTimeRangeAction action:nextActionList) {
                    // Heater
                    HeaterNextActionRowItem mi = new HeaterNextActionRowItem();
                    mi.type = ListItem.HeaterNextActionRow;
                    mi.start = action.start;
                    mi.end = action.end;
                    mi.targetvalue = action.target;
                    mi.scenario = action.scenarioid + "." + action.scenarioname;
                    mi.program = action.programid + "." + action.programname;
                    mi.action = action.actionid + "." + action.actionname;

                    list.add(mi);
                }
                HeaterListListener.HeaterListArrayAdapter adapter = new HeaterListListener.HeaterListArrayAdapter(getActivity(), list, listener);
                listView.setAdapter(adapter);
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






    }
}
