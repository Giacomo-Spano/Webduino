package com.webduino.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.Services;
import com.webduino.WebduinoResponse;
import com.webduino.elements.ProgramActionType;
import com.webduino.elements.ProgramActionTypes;
import com.webduino.elements.Sensor;
import com.webduino.elements.SensorType;
import com.webduino.elements.SensorTypes;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.ActionCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ConditionCardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.DecimalOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.scenarios.Action;
import com.webduino.scenarios.ProgramAction;
import com.webduino.scenarios.Condition;
import com.webduino.scenarios.ScenarioProgram;
import com.webduino.webduinosystems.services.Service;
import com.webduino.zones.Zone;
import com.webduino.zones.ZoneSensor;
import com.webduino.zones.Zones;

import java.util.ArrayList;
import java.util.List;

public class ProgramActionFragment extends Fragment implements ActionFragment.OnActionFragmentListener, ConditionFragment.OnConditionFragmentListener {

    ProgramAction programAction;
    private CardAdapter optionsAdapter, conditionsAdapter, actionsAdapter;
    OptionCardInfo optionCard_Enabled, optionCard_Name, optionCard_Description, optionCard_ActionType, optionCard_Threshold,
            optionCard_Actuator, optionCard_Target, optionCard_Duration, optionCard_Priority,
            optionCard_ServiceId, optionCard_Param;

    CharSequence[] actionItems = new CharSequence[ProgramActionTypes.list.size()];
    int[] actionItemValues = new int[ProgramActionTypes.list.size()];
    int webduinosystemid;

    private OnProgramActionFragmentInteractionListener mListener;

    public ProgramActionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
        webduinosystemid = getArguments().getInt("webduinosystemid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_programaction, container, false);


        // option list
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
                optionCardInfo.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        optionsAdapter.notifyDataSetChanged();
                    }
                });
                optionCardInfo.value.showPicker();
            }
        });

        // condition list
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView conditionList = (RecyclerView) view.findViewById(R.id.conditionList);
        conditionList.setHasFixedSize(true);
        conditionList.setLayoutManager(linearLayoutManager);
        conditionsAdapter = new CardAdapter(this, createConditionList());
        conditionList.setAdapter(conditionsAdapter);
        conditionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onConditionClick(position, cardInfo);
            }
        });

        // action list
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView actionList = (RecyclerView) view.findViewById(R.id.actionList);
        actionList.setHasFixedSize(true);
        actionList.setLayoutManager(linearLayoutManager);
        actionsAdapter = new CardAdapter(this, createActionList());
        actionList.setAdapter(actionsAdapter);
        actionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onActionClick(position, cardInfo);
            }
        });


        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton, getResources().getColor(R.color.colorPrimary), true, getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                programAction.enabled = optionCard_Enabled.value.getBoolValue();
                programAction.name = optionCard_Name.value.getStringValue();
                programAction.description = optionCard_Description.value.getStringValue();
                programAction.type = actionItems[optionCard_ActionType.value.getIntValue()].toString();
                if (optionCard_Threshold != null)
                    programAction.thresholdvalue = optionCard_Threshold.value.getDoubleValue();
                if (optionCard_Actuator != null)
                    programAction.actuatorid = optionCard_Actuator.value.getIntValue();
                if (optionCard_Target != null)
                    programAction.targetvalue = optionCard_Target.value.getDoubleValue();
                if (optionCard_Duration != null)
                    programAction.seconds = optionCard_Duration.value.getIntValue();
                programAction.priority = optionCard_Priority.value.getIntValue();

                if (optionCard_ServiceId != null)
                    programAction.serviceid = optionCard_ServiceId.value.getIntValue();


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

    private void onActionClick(int position, CardInfo cardInfo) {
        if (cardInfo instanceof ActionCardInfo) {
            showActionFragment(((ActionCardInfo) cardInfo).action);
        } else if (cardInfo instanceof ActionButtonCardInfo) {
            createNewAction();
        }
    }

    private void createNewAction() {

        Action action = new Action();
        action.programactionid = programAction.id;

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                Action action = (Action) result;
                programAction.actions.add(action);
                updateActionList();
                showActionFragment(action);

            }
        }, requestDataTask.POST_ACTION).execute(action, false);
    }



    private void updateActionList() {
        List<CardInfo> list = createActionList();
        actionsAdapter.swap(list);
    }

    private void updateConditionList() {
        List<CardInfo> list = createConditionList();
        conditionsAdapter.swap(list);
    }

    private void showActionFragment(Action action) {
        ActionFragment actionFragment = new ActionFragment();
        actionFragment.addListener(this);

        if (action != null) {
            actionFragment.action = action;
            Bundle bundle = new Bundle();
            bundle.putInt("webduinosystemid", webduinosystemid);
            actionFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) actionFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void showConditionFragment(Condition condition) {
        ConditionFragment conditionFragment = new ConditionFragment();
        conditionFragment.addListener(this);

        if (condition != null) {
            conditionFragment.condition = condition;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) conditionFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void onConditionClick(int position, CardInfo cardInfo) {
        if (cardInfo instanceof ConditionCardInfo) {
            showConditionFragment(((ConditionCardInfo) cardInfo).condition);
        } else if (cardInfo instanceof ActionButtonCardInfo) {
            createNewCondition();
        }
    }

    private void createNewCondition() {

        final Condition condition = new Condition();
        condition.programactionid = programAction.id;

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                Condition condition = (Condition) result;
                programAction.conditions.add(condition);
                updateActionList();
                showConditionFragment(condition);
            }
        }, requestDataTask.POST_CONDITION).execute(condition, false);
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
        /*new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    programAction = (ProgramAction) result;
                    if (mListener != null) {
                        mListener.onSaveProgramAction(programAction);
                    }
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        }, requestDataTask.POST_SCENARIOPROGRAMACTION).execute(action, false);*/
    }

    public void deleteProgramAction() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    mListener.onDeleteProgramAction(programAction);
                    getActivity().getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).getScenarioData();
                }
            }
        }, requestDataTask.POST_SCENARIOPROGRAMACTION).execute(programAction, true);
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

    @Override
    public void onSaveAction(Action action) {

    }

    @Override
    public void onDeleteAction(Action action) {

    }

    @Override
    public void onSaveCondition(Condition condition) {

    }

    @Override
    public void onDeleteCondition(Condition condition) {

    }

    public interface OnProgramActionFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveProgramAction(ProgramAction action);

        void onDeleteProgramAction(ProgramAction action);

    }

    private List<CardInfo> createConditionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (Condition condition : programAction.conditions) {
            ConditionCardInfo conditioncardinfo = new ConditionCardInfo();
            conditioncardinfo.id = programAction.id;
            conditioncardinfo.name = programAction.name;
            conditioncardinfo.condition = condition;
            conditioncardinfo.setEnabled(programAction.enabled);
            result.add(conditioncardinfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi condizione";
        //addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    private List<CardInfo> createActionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (Action action : programAction.actions) {
            ActionCardInfo actioncardinfo = new ActionCardInfo();
            actioncardinfo.id = action.id;
            //actioncardinfo.name = "";
            actioncardinfo.action = action;
            actioncardinfo.setEnabled(true);
            result.add(actioncardinfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi condizione";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    public List<CardInfo> createOptionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();

        optionCard_Enabled = new OptionCardInfo();
        optionCard_Enabled.value = new BooleanOptionCardValue("Stato", programAction.enabled, "Abilitato", "Disabilitato");
        result.add(optionCard_Enabled);

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue("Nome", programAction.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue("Descrizione", programAction.description);
        result.add(optionCard_Description);




        // threshold
        if (programAction.hasThreshold()) {
            optionCard_Threshold = new OptionCardInfo();
            optionCard_Threshold.value = new DecimalOptionCardValue("Soglia", programAction.thresholdvalue);
            result.add(optionCard_Threshold);
        }

        // actuator
        if (programAction.hasActuator()) {
            CharSequence[] items = new CharSequence[Sensors.list.size()];
            int[] itemValues = new int[Sensors.list.size()];
            int i = 0;
            for (Sensor sensor : Sensors.list) {
                items[i] = sensor.getName();
                itemValues[i] = sensor.getId();
                i++;
            }
            optionCard_Actuator = new OptionCardInfo();
            optionCard_Actuator.value = new ListOptionCardValue("Attuatore", programAction.actuatorid, items, itemValues);
            result.add(optionCard_Actuator);
        }

        //target
        if (programAction.hasTarget()) {
            optionCard_Target = new OptionCardInfo();
            optionCard_Target.value = new DecimalOptionCardValue("Target", programAction.targetvalue);
            result.add(optionCard_Target);
        }

        //duration
        if (programAction.hasDuration()) {
            optionCard_Duration = new OptionCardInfo();
            optionCard_Duration.value = new IntegerOptionCardValue("Durata", programAction.seconds);
            result.add(optionCard_Duration);
        }

        //priority
        optionCard_Priority = new OptionCardInfo();
        optionCard_Priority.value = new IntegerOptionCardValue("Priorità", programAction.priority);
        result.add(optionCard_Priority);


        // serviceid
        if (programAction.isHasServiceId()) {
            CharSequence[] items = new CharSequence[Services.list.size()];
            int[] itemValues = new int[Services.list.size()];
            int i = 0;
            for (Service service : Services.list) {
                items[i] = service.getName();
                itemValues[i] = service.getId();
                i++;
            }
            optionCard_ServiceId = new OptionCardInfo();
            optionCard_ServiceId.value = new ListOptionCardValue("Service", programAction.serviceid, items, itemValues);
            result.add(optionCard_ServiceId);
        }

        return result;
    }
}
