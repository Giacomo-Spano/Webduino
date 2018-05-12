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
import com.webduino.WebduinoResponse;
import com.webduino.elements.ProgramActionTypes;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.ActionCardInfo;
import com.webduino.fragment.cardinfo.ConditionCardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.TimeOptionCardValue;
import com.webduino.scenarios.Action;
import com.webduino.scenarios.Condition;
import com.webduino.scenarios.ScenarioProgramTimeRange;

import java.util.ArrayList;
import java.util.List;

import static com.webduino.fragment.OptionLoader.ACTION_ACTUATOR;
import static com.webduino.fragment.OptionLoader.CONDITION_ZONESENSORSTATUS;

public class ProgramTimeRangeFragment extends Fragment implements ActionFragment.OnActionFragmentListener, ConditionFragment.OnConditionFragmentListener {

    ScenarioProgramTimeRange timeRange;
    private CardAdapter conditionsAdapter, actionsAdapter, optionsAdapter;
    OptionCardInfo optionCard_Name, optionCard_Description, optionCard_StartTime, optionCard_EndTime, optionCard_Enabled;
    int webduinosystemid;

    private OnProgramTimeRangeFragmentInteractionListener mListener;

    public ProgramTimeRangeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_timerange, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView optionList = (RecyclerView) view.findViewById(R.id.optionList);
        optionList.setHasFixedSize(false);
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


        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView conditionRecyclerView = (RecyclerView) view.findViewById(R.id.conditionList);
        conditionRecyclerView.setHasFixedSize(false);
        conditionRecyclerView.setLayoutManager(linearLayoutManager);
        conditionsAdapter = new CardAdapter(this, createConditionList());
        conditionRecyclerView.setAdapter(conditionsAdapter);
        conditionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onConditionClick(position, cardInfo);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView actionRecyclerView = (RecyclerView) view.findViewById(R.id.actionList);
        actionRecyclerView.setHasFixedSize(false);
        actionRecyclerView.setLayoutManager(linearLayoutManager);
        actionsAdapter = new CardAdapter(this, createActionList());
        actionRecyclerView.setAdapter(actionsAdapter);
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

                timeRange.enabled = optionCard_Enabled.value.getBoolValue();
                timeRange.name = optionCard_Name.value.getStringValue();
                timeRange.description = optionCard_Description.value.getStringValue();
                timeRange.startTime = optionCard_StartTime.value.getDateValue();
                timeRange.endTime = optionCard_EndTime.value.getDateValue();

                saveProgramTimeRange();
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

        ((MainActivity)getActivity()).enableDeleteMenuItem(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteProgramTimeRange();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void saveProgramTimeRange() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                if (!error) {
                    timeRange = (ScenarioProgramTimeRange) result;
                    if (mListener != null) {
                        mListener.onSaveProgramTimeRange(timeRange);
                    }
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        }, requestDataTask.POST_SCENARIOPROGRAMTIMERANGE).execute(timeRange, false);
    }

    public void deleteProgramTimeRange() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    mListener.onDeleteProgramTimeRange(timeRange);
                    getActivity().getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).getScenarioData();
                }
            }
        }, requestDataTask.POST_SCENARIOPROGRAMTIMERANGE).execute(timeRange, true);
    }

    private List<CardInfo> createConditionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (Condition condition : timeRange.conditions) {
            ConditionCardInfo conditioncardinfo = new ConditionCardInfo();
            conditioncardinfo.id = timeRange.id;
            //conditioncardinfo.name = timeRange.name;
            conditioncardinfo.condition = condition;
            conditioncardinfo.setEnabled(timeRange.enabled);
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
        for (Action action : timeRange.actions) {
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
        optionCard_Enabled.value = new BooleanOptionCardValue("Stato", timeRange.enabled, "Abilitato", "Disabilitato");
        result.add(optionCard_Enabled);

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue("Nome", timeRange.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue("Descrizione", timeRange.description);
        result.add(optionCard_Description);

        optionCard_StartTime = new OptionCardInfo();
        optionCard_StartTime.value = new TimeOptionCardValue("Ora inizio", timeRange.startTime);
        result.add(optionCard_StartTime);

        optionCard_EndTime = new OptionCardInfo();
        optionCard_EndTime.value = new TimeOptionCardValue("Ora fine", timeRange.endTime);
        result.add(optionCard_EndTime);
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnProgramTimeRangeFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        condition.type = CONDITION_ZONESENSORSTATUS;
        condition.timerangeid = timeRange.id;

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                Condition condition = (Condition) result;
                timeRange.conditions.add(condition);
                updateConditionList();
                showConditionFragment(condition);
            }
        }, requestDataTask.POST_CONDITION).execute(condition, false);
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
        action.type = ACTION_ACTUATOR;
        action.timerangeid = timeRange.id;

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                Action action = (Action) result;
                timeRange.actions.add(action);
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
            bundle.putInt("timerangeid", timeRange.id);
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
            Bundle bundle = new Bundle();
            bundle.putInt("webduinosystemid", webduinosystemid);
            bundle.putInt("timerangeid", timeRange.id);
            conditionFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) conditionFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    @Override
    public void onSaveAction(Action action) {

    }

    @Override
    public void onDeleteAction(Action action) {
        timeRange.actions.remove(action);
    }

    @Override
    public void onSaveCondition(Condition condition) {

    }

    @Override
    public void onDeleteCondition(Condition condition) {
        timeRange.conditions.remove(condition);
    }



    public interface OnProgramTimeRangeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveProgramTimeRange(ScenarioProgramTimeRange timeRange);
        void onDeleteProgramTimeRange(ScenarioProgramTimeRange timeRange);
    }
}
