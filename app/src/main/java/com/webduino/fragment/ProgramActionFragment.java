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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.WebduinoResponse;
import com.webduino.elements.Actuator;
import com.webduino.elements.ProgramActionType;
import com.webduino.elements.ProgramActionTypes;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.DateOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.DecimalOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.scenarios.ProgramAction;
import com.webduino.scenarios.ScenarioProgramTimeRange;
import com.webduino.scenarios.ScenarioTrigger;
import com.webduino.zones.Zone;
import com.webduino.zones.Zones;

import java.util.ArrayList;
import java.util.List;

public class ProgramActionFragment extends Fragment {

    ProgramAction action;
    private CardAdapter optionsAdapter;
    OptionCardInfo optionCard_Enabled, optionCard_Name, optionCard_Description, optionCard_ActionType, optionCard_Zone, optionCard_Threshold, optionCard_Actuator, optionCard_Target, optionCard_Duration, optionCard_Priority;

    CharSequence[] actionItems = new CharSequence[ProgramActionTypes.list.size()];
    int[] actionItemValues = new int[ProgramActionTypes.list.size()];

    private OnProgramActionFragmentInteractionListener mListener;

    public ProgramActionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_action, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView optionList = (RecyclerView) view.findViewById(R.id.optionList);
        optionList.setHasFixedSize(true);
        optionList.setLayoutManager(linearLayoutManager);
        optionsAdapter = new CardAdapter(this, createOptionList());
        optionList.setAdapter(optionsAdapter);
        final Fragment fragment = this;

        optionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                OptionCardInfo optionCardInfo = (OptionCardInfo) cardInfo;
                optionCardInfo.value.setListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {

                        /*action.type = actionItems[optionCard_ActionType.value.getIntValue()].toString();

                        List<CardInfo> list = createOptionList();
                        for (CardInfo card:list) {
                            //optionsAdapter.swap(list);
                        }*/
                        //optionsAdapter = new CardAdapter(fragment, createOptionList());
                        //optionList.setAdapter(optionsAdapter);
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

                action.enabled = optionCard_Enabled.value.getBoolValue();
                action.name = optionCard_Name.value.getStringValue();
                action.description = optionCard_Description.value.getStringValue();
                action.type = actionItems[optionCard_ActionType.value.getIntValue()].toString();
                if (optionCard_Zone != null)
                    action.zoneid = optionCard_Zone.value.getIntValue();
                if (optionCard_Threshold != null)
                    action.thresholdvalue = optionCard_Threshold.value.getDoubleValue();
                if (optionCard_Actuator != null)
                    action.actuatorid = optionCard_Actuator.value.getIntValue();
                if (optionCard_Target != null)
                    action.targetvalue = optionCard_Target.value.getDoubleValue();
                if (optionCard_Duration != null)
                    action.seconds = optionCard_Duration.value.getIntValue();
                action.priority = optionCard_Priority.value.getIntValue();

                saveProgramAction();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteProgramAction();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void saveProgramAction() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    action = (ProgramAction) result;
                    if (mListener != null) {
                        mListener.onSaveProgramAction(action);
                    }
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        }, requestDataTask.POST_SCENARIOPROGRAMACTION).execute(action, false);
    }

    public void deleteProgramAction() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    mListener.onDeleteProgramAction(action);
                    getActivity().getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).getScenarioData();
                }
            }
        }, requestDataTask.POST_SCENARIOPROGRAMACTION).execute(action, true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnProgramActionFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnProgramActionFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveProgramAction(ProgramAction action);

        void onDeleteProgramAction(ProgramAction action);

    }

    public List<CardInfo> createOptionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();

        optionCard_Enabled = new OptionCardInfo();
        optionCard_Enabled.value = new BooleanOptionCardValue("Stato", action.enabled, "Abilitato", "Disabilitato");
        result.add(optionCard_Enabled);

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue("Nome", action.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue("Descrizione", action.description);
        result.add(optionCard_Description);

        // action type
        int i = 0;
        int currentValue = 0;
        for (ProgramActionType actionType : ProgramActionTypes.list) {
            actionItems[i] = actionType.instruction;
            actionItemValues[i] = i;
            if (actionType.instruction.equals(action.type))
                currentValue = i;
            i++;
        }
        optionCard_ActionType = new OptionCardInfo();
        optionCard_ActionType.value = new ListOptionCardValue("Tipo", currentValue, actionItems, actionItemValues);
        result.add(optionCard_ActionType);

        if (action.hasZone()) {
            CharSequence[] items = new CharSequence[ProgramActionTypes.list.size()];
            int[] itemValues = new int[ProgramActionTypes.list.size()];
            // zona
            items = new CharSequence[Zones.list.size()];
            itemValues = new int[Zones.list.size()];
            i = 0;
            for (Zone zone : Zones.list) {
                items[i] = zone.name;
                itemValues[i] = zone.id;
                i++;
            }
            optionCard_Zone = new OptionCardInfo();
            optionCard_Zone.value = new ListOptionCardValue("Zona", action.zoneid, items, itemValues);
            result.add(optionCard_Zone);
        }

        // threshold
        if (action.hasThreshold()) {
            optionCard_Threshold = new OptionCardInfo();
            optionCard_Threshold.value = new DecimalOptionCardValue("Soglia", action.thresholdvalue);
            result.add(optionCard_Threshold);
        }

        // actuator
        if (action.hasActuator()) {
            CharSequence[] items = new CharSequence[Sensors.list.size()];
            int[] itemValues = new int[Sensors.list.size()];
            i = 0;
            for (Sensor sensor : Sensors.list) {
                //if (sensor instanceof Actuator) {
                //if (sensor instanceof Actuator) {
                items[i] = sensor.getName();
                itemValues[i] = sensor.getId();
                //}
                //}
                i++;
            }
            optionCard_Actuator = new OptionCardInfo();
            optionCard_Actuator.value = new ListOptionCardValue("Attuatore", action.actuatorid, items, itemValues);
            result.add(optionCard_Actuator);
        }

        //target
        if (action.hasTarget()) {
            optionCard_Target = new OptionCardInfo();
            optionCard_Target.value = new DecimalOptionCardValue("Target", action.targetvalue);
            result.add(optionCard_Target);
        }

        //duration
        if (action.hasDuration()) {
            optionCard_Duration = new OptionCardInfo();
            optionCard_Duration.value = new IntegerOptionCardValue("Durata", action.seconds);
            result.add(optionCard_Duration);
        }

        //priority
        optionCard_Priority = new OptionCardInfo();
        optionCard_Priority.value = new IntegerOptionCardValue("Priorità", action.priority);
        result.add(optionCard_Priority);

        return result;
    }
}
