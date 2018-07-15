package com.webduino.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.webduino.ActionCommand;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.Services;
import com.webduino.WebduinoResponse;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.Trigger;
import com.webduino.elements.Triggers;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.scenarios.Action;
import com.webduino.webduinosystems.WebduinoSystem;
import com.webduino.webduinosystems.WebduinoSystems;
import com.webduino.webduinosystems.services.Service;

import java.util.ArrayList;
import java.util.List;

public class ActionFragment extends Fragment {

    Action action;
    private CardAdapter optionsAdapter;
    OptionCardInfo optionCard_ActionType,
            optionCard_ActuatorId,
            optionCard_ServiceId,
            optionCard_ActuatorCommand,
            optionCard_ServiceCommand,
            optionCard_TriggerCommand,
            optionCard_ZoneId,
            optionCard_ZoneSensorId,
            optionCard_TriggerId,
            optionCard_TargetValue,
            optionCard_IntegerValue,
            optionCard_ParamValue,
            optionCard_DeviceValue;
    OptionLoader loader = new OptionLoader();
    private WebduinoSystem webduinoSystem;
    private List<CardInfo> result = new ArrayList<>();

    private OnActionFragmentListener mListener;

    public ActionFragment() {
        optionCard_ActuatorCommand = new OptionCardInfo();
        optionCard_ServiceCommand = new OptionCardInfo();
        optionCard_TriggerCommand = new OptionCardInfo();
        optionCard_ActuatorId = new OptionCardInfo();
        optionCard_ServiceId = new OptionCardInfo();
        optionCard_ActionType = new OptionCardInfo();
        optionCard_ZoneId = new OptionCardInfo();
        optionCard_ZoneSensorId = new OptionCardInfo();
        optionCard_TriggerId = new OptionCardInfo();
        optionCard_TargetValue = new OptionCardInfo();
        optionCard_IntegerValue = new OptionCardInfo();
        optionCard_ParamValue = new OptionCardInfo();
        optionCard_DeviceValue = new OptionCardInfo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
        int webduinosystemid = getArguments().getInt("webduinosystemid");
        webduinoSystem = WebduinoSystems.getFromId(webduinosystemid);
        action.timerangeid = getArguments().getInt("timerangeid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView optionList = (RecyclerView) view.findViewById(R.id.optionList);
        optionList.setHasFixedSize(true);
        optionList.setLayoutManager(linearLayoutManager);
        optionsAdapter = new CardAdapter(this, createOptionList());
        optionList.setAdapter(optionsAdapter);
        //optionsAdapter.notifyDataSetChanged();
        optionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                OptionCardInfo optionCardInfo = (OptionCardInfo) cardInfo;
                if (((OptionCardInfo) cardInfo).value != null) {
                    optionCardInfo.value.addListener(new OptionCardValue.OptionCardListener() {
                        @Override
                        public void onSetValue(Object value) {
                            optionsAdapter.notifyDataSetChanged();
                        }
                    });
                    optionCardInfo.value.showPicker();
                }
            }
        });

        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton, getResources().getColor(R.color.colorPrimary), true, getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (optionCard_ActionType.value != null)
                    action.type = optionCard_ActionType.value.getStringValue();

                if (optionCard_ActuatorId.value != null)
                    action.actuatorid = optionCard_ActuatorId.value.getIntValue();

                if (optionCard_ServiceId.value != null)
                    action.serviceid = optionCard_ServiceId.value.getIntValue();

                if (optionCard_ActuatorCommand.value != null)
                    action.actuatorcommand = optionCard_ActuatorCommand.value.getStringValue();

                if (optionCard_ServiceCommand.value != null)
                    action.servicecommand = optionCard_ServiceCommand.value.getStringValue();

                if (optionCard_TriggerCommand.value != null)
                    action.triggercommand = optionCard_TriggerCommand.value.getStringValue();

                if (optionCard_ZoneId.value != null)
                    action.zoneid = optionCard_ZoneId.value.getIntValue();

                if (optionCard_ZoneSensorId.value != null)
                    action.zonesensorid = optionCard_ZoneSensorId.value.getIntValue();

                if (optionCard_TriggerId.value != null)
                    action.triggerid = optionCard_TriggerId.value.getIntValue();

                if (optionCard_TargetValue.value != null)
                    action.targetvalue = optionCard_TargetValue.value.getDoubleValue();

                if (optionCard_ParamValue.value != null)
                    action.param = optionCard_ParamValue.value.getStringValue();

                if (optionCard_DeviceValue.value != null)
                    action.deviceid = optionCard_DeviceValue.value.getIntValue();


                saveAction();
            }
        });

        Button cancelbutton = view.findViewById(R.id.cancelButton);
        MainActivity.setImageButton(cancelbutton, getResources().getColor(R.color.colorPrimary), false, getResources().getDrawable(R.drawable.uncheck));
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        ((MainActivity) getActivity()).enableDeleteMenuItem(true);
        return view;
    }

    public void saveAction() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    action = (Action) result;
                    if (mListener != null) {
                        mListener.onSaveAction(action);
                    }
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        }, requestDataTask.POST_ACTION).execute(action, false);
    }

    public void deleteAction() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    mListener.onDeleteAction(action);
                    getActivity().getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).getScenarioData();
                }
            }
        }, requestDataTask.POST_ACTION).execute(action, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteAction();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnActionFragmentListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnActionFragmentListener {
        // TODO: Update argument type and name
        void onSaveAction(Action action);

        void onDeleteAction(Action action);
    }

    public List<CardInfo> createOptionList() {
        result = new ArrayList<CardInfo>();
        loadOptions();
        return result;
    }

    private void loadOptions() {

        if (webduinoSystem == null) return;

        result.clear();

        loader.loadActionType(optionCard_ActionType, action.type);
        result.add(optionCard_ActionType);
        optionCard_ActionType.value.addListener(new OptionCardValue.OptionCardListener() {
            @Override
            public void onSetValue(Object value) {
                action.type = (String) value;
                loadOptions();
            }
        });

        ActionCommand actionCommand = null;
        if (action.type.equals("actuator")) {
            final Sensor actuator = Sensors.getFromId(action.actuatorid);
            if (actuator != null) {
                actionCommand = actuator.getActionCommand(action.actuatorcommand);
            }
            loader.loadActuatorId(optionCard_ActuatorId, action.actuatorid, webduinoSystem);
            result.add(optionCard_ActuatorId);
            optionCard_ActuatorId.value.addListener(new OptionCardValue.OptionCardListener() {
                @Override
                public void onSetValue(Object value) {
                    action.actuatorid = (int) value;
                    loadOptions();
                }
            });
            if (loader.loadActuatorCommand(optionCard_ActuatorCommand, action.actuatorid, action.actuatorcommand)) {
                optionCard_ActuatorCommand.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        action.actuatorcommand = (String) value;
                        loadOptions();
                    }
                });
                result.add(optionCard_ActuatorCommand);
                loadCommandParams(result, actionCommand);

            }

        } else if (action.type.equals("service")) {

            Service service = Services.getFromId(action.serviceid);
            if (service != null) {
                actionCommand = service.getActionCommand(action.servicecommand);

                loader.loadWebduinoSystemServiceId(optionCard_ServiceId, action.serviceid, webduinoSystem);
                result.add(optionCard_ServiceId);
                optionCard_ServiceId.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        action.serviceid = (int) value;
                        loadOptions();
                    }
                });
                if (loader.loadServiceCommand(optionCard_ServiceCommand, action.serviceid, action.servicecommand)) {
                    optionCard_ServiceCommand.value.addListener(new OptionCardValue.OptionCardListener() {
                        @Override
                        public void onSetValue(Object value) {
                            action.servicecommand = (String) value;
                            loadOptions();
                        }
                    });
                    result.add(optionCard_ServiceCommand);
                    loadCommandParams(result, actionCommand);
                }
            }
        } else if (action.type.equals("trigger")) {
            Trigger trigger = Triggers.getFromId(action.triggerid);
            if (trigger != null) {
                actionCommand = trigger.getActionCommand(action.actuatorcommand);

                loader.loadTriggerId(optionCard_TriggerId, action.triggerid);
                result.add(optionCard_TriggerId);
                optionCard_TriggerId.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        action.triggerid = (int) value;
                        loadOptions();
                    }
                });
                loader.loadTriggerCommand("Comando trigger", optionCard_TriggerCommand, trigger.id, action.triggercommand);
                result.add(optionCard_TriggerCommand);
            }
        }
    }

    private void loadCommandParams(final List<CardInfo> result, ActionCommand actionCommand) {
        if (actionCommand != null && actionCommand.hasTarget()) {
            loader.loadDecimalValue(actionCommand.name, optionCard_TargetValue, action.targetvalue);
            optionCard_TargetValue.value.addListener(new OptionCardValue.OptionCardListener() {
                @Override
                public void onSetValue(Object value) {
                    action.targetvalue = (double) value;
                    //loadOptions(value, result);
                }
            });
            result.add(optionCard_TargetValue);
        }

        if (actionCommand != null && actionCommand.hasParam()) {
            loader.loadStringValue(actionCommand.paramname, optionCard_ParamValue, action.param);
            optionCard_ParamValue.value.addListener(new OptionCardValue.OptionCardListener() {
                @Override
                public void onSetValue(Object value) {
                    action.param = (String) value;
                    //loadOptions(value, result);
                }
            });
            result.add(optionCard_ParamValue);
        }

        if (actionCommand != null && actionCommand.hasDevice()) {
            loader.loadDevice(optionCard_DeviceValue, action.deviceid);
            optionCard_DeviceValue.value.addListener(new OptionCardValue.OptionCardListener() {
                @Override
                public void onSetValue(Object value) {
                    action.deviceid = (int) value;
                    //loadOptions();
                    loader.loadDevice(optionCard_DeviceValue, action.deviceid);
                }
            });
            result.add(optionCard_DeviceValue);
        }

        if (actionCommand != null && actionCommand.hasZone()) {
            loader.loadZoneId(optionCard_ZoneId, action.zoneid);

            final String type = actionCommand.getZoneSensorType();
            if (optionCard_ZoneId.value != null) {
                optionCard_ZoneId.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        action.zoneid = (int) value;
                        loadOptions();
                    }
                });
                result.add(optionCard_ZoneId);
            }
            loader.loadZoneSensorId(optionCard_ZoneSensorId, action.zoneid, action.zonesensorid, type);
            if (optionCard_ZoneSensorId.value != null) {
                optionCard_ZoneSensorId.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        action.zonesensorid = (int) value;
                        //loadOptions(value, result);
                    }
                });

            result.add(optionCard_ZoneSensorId);
            }
        }
    }
}
