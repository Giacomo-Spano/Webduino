package com.webduino.fragment;

import com.webduino.AsyncRequestDataResponse;
import com.webduino.HeaterActivity;
import com.webduino.MainActivity;
import com.webduino.elements.Actuator;
import com.webduino.elements.CommandDataLog;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.HeaterCommandLogRowItem;
import com.webduino.fragment.adapters.HeaterDataRowItem;
import com.webduino.fragment.adapters.HeaterListListener;
import com.webduino.fragment.adapters.ListItem;
import com.webduino.scenarios.Scenario;
import com.webduino.zones.Zone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by giaco on 07/01/2018.
 */

public class HeaterLogPageFragment extends PageFragment {

    public static PageFragment newInstance() {
        PageFragment fragment = new HeaterLogPageFragment();
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

            new requestDataTask(MainActivity.activity, new AsyncRequestDataResponse() {
                @Override
                public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                    List<CommandDataLog> commandLogList = (List<CommandDataLog>) result;
                    list = new ArrayList<>();
                    Date currentDate = null;
                    for (CommandDataLog commandlog:commandLogList) {

                        HeaterCommandLogRowItem mi = new HeaterCommandLogRowItem();
                        mi.type = ListItem.HeaterCommandLogRow;
                        mi.date = commandlog.date;
                        mi.enddate = commandlog.enddate;
                        mi.targetvalue = commandlog.target;
                        mi.temperature = commandlog.temperature;
                        mi.success = commandlog.success;
                        mi.duration = commandlog.duration;
                        mi.command = commandlog.command;

                        list.add(mi);
                    }
                    HeaterListListener.HeaterListArrayAdapter adapter = new HeaterListListener.HeaterListArrayAdapter(getActivity(), list, listener);
                    listView.setAdapter(adapter);
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
            }, requestDataTask.REQUEST_COMMANDDATALOG).execute(id);
    }
}
