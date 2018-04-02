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
import com.webduino.elements.ProgramActionType;
import com.webduino.elements.ProgramActionTypes;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.Trigger;
import com.webduino.elements.Triggers;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.scenarios.Action;
import com.webduino.webduinosystems.services.Service;

import java.util.ArrayList;
import java.util.List;

public class ActionFragment extends Fragment {

    Action action;
    private CardAdapter optionsAdapter;
    OptionCardInfo optionCard_ActionType,
                    optionCard_ActuatorId,
                    optionCard_ServiceId,
                optionCard_ActionCommand, optionCard_ActionId, optionCard_Priority;

    private OnActionFragmentListener mListener;

    public ActionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
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
        optionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                OptionCardInfo optionCardInfo = (OptionCardInfo) cardInfo;
                optionCardInfo.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        optionsAdapter.notifyDataSetChanged();
                    }
                });
                optionCardInfo.value.showPicker();
            }
        });

        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton, getResources().getColor(R.color.colorPrimary), true, getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*action.name = optionCard_Name.value.getStringValue();
                action.description = optionCard_Description.value.getStringValue();
                action.triggerid = optionCard_ActionId.value.getIntValue();
                action.priority = optionCard_Priority.value.getIntValue();
                action.enabled = optionCard_Enabled.value.getBoolValue();*/
                saveTrigger();
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

    public void saveTrigger() {
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

    public void deleteTrigger() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    mListener.onDeleteAction(action);
                    getActivity().getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).getScenarioData();
                }
            }
        }, requestDataTask.POST_ACTION).execute(action, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteTrigger();
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
        final List<CardInfo> result = new ArrayList<CardInfo>();

        // actuator id
        CharSequence[] items = new CharSequence[Sensors.list.size()];
        int[] itemValues = new int[Sensors.list.size()];
        String[] itemStringValues = new String[Sensors.list.size()];
        int i = 0;
        for (Sensor sensor : Sensors.list) {
            items[i] = sensor.name;
            itemValues[i] = sensor.id;
            i++;
        }
        optionCard_ActuatorId = new OptionCardInfo();
        optionCard_ActuatorId.value = new ListOptionCardValue("Attuatore", action.actuatorid, items, itemValues);

        // service id
        items = new CharSequence[Services.list.size()];
        itemValues = new int[Services.list.size()];
        i = 0;
        for (Service service : Services.list) {
            items[i] = service.name;
            itemValues[i] = service.id;
            i++;
        }
        optionCard_ServiceId = new OptionCardInfo();
        optionCard_ServiceId.value = new ListOptionCardValue("Servizio", action.serviceid, items, itemValues);

        // action command
        if (action.type.equals("actuator")) {
            Sensor actuator = Sensors.getFromId(action.actuatorid);
            if (actuator != null) {
                items = new CharSequence[actuator.actioncommandlist.size()];
                itemStringValues = new String[actuator.actioncommandlist.size()];
                i = 0;
                for (ActionCommand actionCommand : actuator.actioncommandlist) {
                    items[i] = actionCommand.command;
                    itemStringValues[i] = actionCommand.name;
                    i++;
                }
            }
        } else if (action.type.equals("service")) {
            Service service = Services.getFromId(action.serviceid);
            if (service != null) {
                items = new CharSequence[service.actioncommandlist.size()];
                itemStringValues = new String[service.actioncommandlist.size()];
                i = 0;
                for (ActionCommand actionCommand : service.actioncommandlist) {
                    items[i] = actionCommand.command;
                    itemStringValues[i] = actionCommand.name;
                    i++;
                }
            }
        } else {
            items = new CharSequence[0];
            itemStringValues = new String[0];
        }
        optionCard_ActionCommand = new OptionCardInfo();
        optionCard_ActionCommand.value = new ListOptionCardValue("Servizio", action.serviceid, items, itemValues);

        // actuator or service
        CharSequence[] cs = {"Attuatore", "Servizio"};
        String[] csvalue = {"actuator", "service"};
        optionCard_ActionType = new OptionCardInfo();
        optionCard_ActionType.value = new ListOptionCardValue("Tipo", cs[0].toString(), cs, csvalue);
        optionCard_ActionType.value.addListener(new OptionCardValue.OptionCardListener() {
            @Override
            public void onSetValue(Object value) {
                if (value.equals("actuator")) {
                    result.add(optionCard_ActuatorId);
                    result.remove(optionCard_ServiceId);
                } else {
                    result.add(optionCard_ServiceId);
                    result.remove(optionCard_ActuatorId);
                }
            }
        });
        result.add(optionCard_ActionType);
        result.remove(optionCard_ActionCommand);

        return result;
    }
}
