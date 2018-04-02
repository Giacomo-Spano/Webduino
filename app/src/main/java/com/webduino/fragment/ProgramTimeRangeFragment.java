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
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.ProgramActionCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.TimeOptionCardValue;
import com.webduino.scenarios.ProgramAction;
import com.webduino.scenarios.ScenarioProgramTimeRange;

import java.util.ArrayList;
import java.util.List;

public class ProgramTimeRangeFragment extends Fragment implements ProgramActionFragment.OnProgramActionFragmentInteractionListener {

    ScenarioProgramTimeRange timeRange;
    private CardAdapter programActionsAdapter, optionsAdapter;
    OptionCardInfo optionCard_Name, optionCard_Description, optionCard_StartTime, optionCard_EndTime, optionCard_Enabled;

    private OnProgramTimeRangeFragmentInteractionListener mListener;

    public ProgramTimeRangeFragment() {
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
        RecyclerView actionRecyclerView = (RecyclerView) view.findViewById(R.id.actionList);
        actionRecyclerView.setHasFixedSize(false);
        actionRecyclerView.setLayoutManager(linearLayoutManager);
        programActionsAdapter = new CardAdapter(this, createProgramActionList());
        actionRecyclerView.setAdapter(programActionsAdapter);
        programActionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onProgramActionClick(position, cardInfo);
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

    private List<CardInfo> createProgramActionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (ProgramAction action : timeRange.programActionList) {
            ProgramActionCardInfo actioncardinfo = new ProgramActionCardInfo();
            actioncardinfo.id = action.id;
            actioncardinfo.name = action.name;
            actioncardinfo.action = action;
            actioncardinfo.setEnabled(action.enabled);
            result.add(actioncardinfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi azione";
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

    public void onProgramActionClick(int position, CardInfo cardInfo) {
        if (cardInfo instanceof ProgramActionCardInfo) {
            showActionFragment(((ProgramActionCardInfo) cardInfo).action);
        } else if (cardInfo instanceof ActionButtonCardInfo) {
            createNewProgramAction();
        }
    }

    private void createNewProgramAction() {
        ProgramAction action = new ProgramAction();
        action.timerangeid = timeRange.id;

        if (ProgramActionTypes.list == null || ProgramActionTypes.list.size() == 0)
            return;

        action.type = ProgramActionTypes.list.get(0).instruction; // questo serve per inizializzare il tipo di action altrimenti
        // il salvataggio fallisce

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                ProgramAction action = (ProgramAction) result;
                timeRange.programActionList.add(action);
                updateProgramActionList();
                showActionFragment(action);
            }

        }, requestDataTask.POST_SCENARIOPROGRAMACTION).execute(action, false);
    }

    private void updateProgramActionList() {
        List<CardInfo> list = createProgramActionList();
        programActionsAdapter.swap(list);
    }

    private void showActionFragment(ProgramAction programAction) {
        ProgramActionFragment programActionFragment = new ProgramActionFragment();
        programActionFragment.addListener(this);

        if (programAction != null) {
            programActionFragment.programAction = programAction;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) programActionFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onSaveProgramAction(ProgramAction savedaction) {

        ProgramAction action = timeRange.getProgramActionFromId(savedaction.id);

        if (savedaction.id == 0) {
            timeRange.programActionList.add(savedaction);
        } else if (action != null) {
            for (ProgramAction programAction : timeRange.programActionList) {
                if (programAction.id == savedaction.id) {
                    int itemIndex = timeRange.programActionList.indexOf(programAction);
                    if (itemIndex != -1) {
                        timeRange.programActionList.set(itemIndex, savedaction);
                    }
                    return;
                }
            }
        }
        mListener.onSaveProgramTimeRange(timeRange);
    }

    @Override
    public void onDeleteProgramAction(ProgramAction action) {
        timeRange.programActionList.remove(action);
    }


    public interface OnProgramTimeRangeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveProgramTimeRange(ScenarioProgramTimeRange timeRange);
        void onDeleteProgramTimeRange(ScenarioProgramTimeRange timeRange);
    }
}
