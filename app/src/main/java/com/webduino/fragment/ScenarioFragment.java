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
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.adapters.HeaterDataRowItem;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.TimeIntervalCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ProgramCardInfo;
import com.webduino.fragment.cardinfo.TriggerCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.scenarios.ProgramAction;
import com.webduino.scenarios.Scenario;
import com.webduino.scenarios.ScenarioProgram;
import com.webduino.scenarios.ScenarioProgramTimeRange;
import com.webduino.scenarios.ScenarioTimeInterval;
import com.webduino.scenarios.ScenarioTrigger;
import com.webduino.scenarios.Scenarios;
import com.webduino.webduinosystems.WebduinoSystem;
import com.webduino.webduinosystems.WebduinoSystems;

import java.util.ArrayList;
import java.util.List;

public class ScenarioFragment extends Fragment implements
        TimeIntervalFragment.OnTimeIntervalFragmentInteractionListener, TriggerFragment.OnTriggerFragmentInteractionListener, ProgramFragment.OnProgramFragmentInteractionListener,
       ProgramTimeRangeFragment.OnProgramTimeRangeFragmentInteractionListener, ProgramActionFragment.OnProgramActionFragmentInteractionListener {

    public boolean adaptercreated = false;
    ArrayList<HeaterDataRowItem> list = new ArrayList<>();
    OnScenarioFragmentInteractionListener mListener;
    private Scenario scenario;
    private CardAdapter optionsAdapter, calendarsAdapter, triggersAdapter, programsAdapter;
    private int webduinosystemid;

    OptionCardInfo optionCard_Name, optionCard_Description, optionCard_Priority, optionCard_Enabled;

    public interface OnScenarioFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSave(Scenario scenario);
    }

    public void setListener(OnScenarioFragmentInteractionListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }
        View view = inflater.inflate(R.layout.fragment_scenario, container, false);

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
        RecyclerView calendarList = (RecyclerView) view.findViewById(R.id.calendarList);
        calendarList.setHasFixedSize(false);
        calendarList.setLayoutManager(linearLayoutManager);
        calendarsAdapter = new CardAdapter(this, createCalendarList());
        calendarList.setAdapter(calendarsAdapter);
        calendarsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onTimeintervalClick(position,cardInfo);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView triggerList = (RecyclerView) view.findViewById(R.id.triggerList);
        triggerList.setHasFixedSize(false);
        triggerList.setLayoutManager(linearLayoutManager);
        triggersAdapter = new CardAdapter(this, createTriggerList());
        triggerList.setAdapter(triggersAdapter);
        triggersAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onTriggerClick(position,cardInfo);
            }
        });
        //triggerList.setNestedScrollingEnabled(false);

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView programList = (RecyclerView) view.findViewById(R.id.programList);
        programList.setHasFixedSize(false);
        programList.setLayoutManager(linearLayoutManager);
        programsAdapter = new CardAdapter(this, createProgramList());
        programList.setAdapter(programsAdapter);
        programsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onProgramClick(position,cardInfo);
            }
        });

        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton,getResources().getColor(R.color.colorPrimary),true,getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scenario.name = optionCard_Name.value.getStringValue();
                scenario.description = optionCard_Description.value.getStringValue();
                Object val = optionCard_Enabled.value;
                scenario.enabled = optionCard_Enabled.value.getBoolValue();
                scenario.priority = optionCard_Enabled.value.getIntValue();

                saveScenario();
                if (mListener != null) {
                    mListener.onSave(scenario);
                }

            }
        });

        Button cancelbutton = view.findViewById(R.id.cancelButton);
        MainActivity.setImageButton(cancelbutton,getResources().getColor(R.color.colorPrimary),false,getResources().getDrawable(R.drawable.uncheck));

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        ((MainActivity) getActivity()).hideFloatingActionButton();

        ((MainActivity)getActivity()).enableDeleteMenuItem(true);

        return view;
    }


    public void onTimeintervalClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof TimeIntervalCardInfo) {

            TimeIntervalCardInfo timeIntervalCardInfo = (TimeIntervalCardInfo) cardInfo;
            showTimeintervalFragment(timeIntervalCardInfo);

        } else if (cardInfo instanceof ActionButtonCardInfo) {

            TimeIntervalCardInfo timeIntervalCardInfo = new TimeIntervalCardInfo();
            showTimeintervalFragment(timeIntervalCardInfo);
        }
    }

    public void onTriggerClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof TriggerCardInfo) {

            TriggerCardInfo triggerCardInfo = (TriggerCardInfo) cardInfo;
            showTriggerFragment(triggerCardInfo);

        } else if (cardInfo instanceof ActionButtonCardInfo) {

            TriggerCardInfo triggerCardInfo = new TriggerCardInfo();
            showTriggerFragment(triggerCardInfo);
        }
    }

    public void onProgramClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof ProgramCardInfo) {

            showProgramFragment(((ProgramCardInfo)cardInfo).program);

        } else if (cardInfo instanceof ActionButtonCardInfo) {
            createNewProgram();
        }
    }

    private void showTimeintervalFragment(TimeIntervalCardInfo timeIntervalCardInfo) {
        TimeIntervalFragment timeIntervalFragment = new TimeIntervalFragment();
        timeIntervalFragment.addListener(this);
        ScenarioTimeInterval timeInterval;
        if (timeIntervalCardInfo.id == 0)
            timeInterval = new ScenarioTimeInterval();
        else
            timeInterval = timeIntervalCardInfo.timeInterval;

        if (timeInterval != null) {
            timeIntervalFragment.timeInterval = timeInterval;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) timeIntervalFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void showTriggerFragment(TriggerCardInfo triggerCardInfo) {
        TriggerFragment triggerFragment = new TriggerFragment();
        triggerFragment.addListener(this);

        ScenarioTrigger trigger;
        if (triggerCardInfo.id == 0)
            trigger = new ScenarioTrigger();
        else
            trigger = triggerCardInfo.trigger;

        if (trigger != null){
            triggerFragment.trigger = trigger;

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, (Fragment) triggerFragment);
        ft.addToBackStack(null);
        ft.commit();
        }
    }

    private void createNewProgram() {
        ScenarioProgram program = new ScenarioProgram();
        program.scenarioId = scenario.id;

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                ScenarioProgram program = (ScenarioProgram) result;
                scenario.programs.add(program);
                updateProgramList();
                showProgramFragment(program);

            }
        }, requestDataTask.POST_SCENARIOPROGRAM).execute(program,false);
    }

    private void showProgramFragment(ScenarioProgram program) {
        ProgramFragment programFragment = new ProgramFragment();
        programFragment.addListener(this);

        if (program != null) {
            programFragment.program = program;
            Bundle bundle = new Bundle();
            bundle.putInt("webduinosystemid", webduinosystemid);
            programFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) programFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public List<CardInfo> createOptionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue("Nome", scenario.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue("Descrizione", scenario.description);
        result.add(optionCard_Description);

        optionCard_Priority = new OptionCardInfo();
        optionCard_Priority.value = new IntegerOptionCardValue("Priorità", scenario.priority);
        result.add(optionCard_Priority);

        optionCard_Enabled = new OptionCardInfo();
        optionCard_Enabled.value = new BooleanOptionCardValue("Stato", scenario.enabled, "Abilitato", "Disabilitato");
        result.add(optionCard_Enabled);

        return result;
    }

    public void updateProgramList() {

        List<CardInfo> list = createProgramList();
        programsAdapter.swap(list);
        /*if (scenarioFragment != null) {
            //scenarioFragment.refreshData();
        }*/
    }

    public List<CardInfo> createCalendarList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (ScenarioTimeInterval timeInterval : scenario.calendar.timeIntervals) {
            TimeIntervalCardInfo timeintervalcardinfo = new TimeIntervalCardInfo();
            timeintervalcardinfo.id = timeInterval.id;
            timeintervalcardinfo.name = timeInterval.name;
            timeintervalcardinfo.timeInterval = timeInterval;
            timeintervalcardinfo.setEnabled(timeInterval.enabled);
            result.add(timeintervalcardinfo);
        }

        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    public List<CardInfo> createTriggerList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (ScenarioTrigger trigger : scenario.triggers) {
            TriggerCardInfo triggercardinfo = new TriggerCardInfo();
            triggercardinfo.id = trigger.id;
            triggercardinfo.name = trigger.name;
            triggercardinfo.trigger = trigger;
            triggercardinfo.setEnabled(trigger.enabled);
            result.add(triggercardinfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    public List<CardInfo> createProgramList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (ScenarioProgram program : scenario.programs) {
            ProgramCardInfo programCardInfo = new ProgramCardInfo();
            programCardInfo.id = program.id;
            programCardInfo.name = program.name;
            programCardInfo.program = program;
            programCardInfo.setEnabled(program.enabled);
            result.add(programCardInfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteScenario();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int scenarioid = getArguments().getInt("scenarioid");
        webduinosystemid = getArguments().getInt("webduinosystemid");
        if (scenarioid == 0)
            scenario = new Scenario(webduinosystemid);
        else
            scenario = Scenarios.getFromId(scenarioid);
    }


    @Override
    public void onResume() {
        super.onResume();
    }



    public void createAdapter() {
        adaptercreated = true;
    }

    public void destroyAdapter() {
        adaptercreated = false;
    }

    public void saveScenario()  {

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {

            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
               scenario = (Scenario) result;
               mListener.onSave(scenario);
               getActivity().getFragmentManager().popBackStack();
            }

        }, requestDataTask.POST_SCENARIO).execute(scenario,false);
    }

    public void deleteScenario()  {

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {

            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                getActivity().getFragmentManager().popBackStack();
                ((MainActivity)getActivity()).getScenarioData();
            }

        }, requestDataTask.POST_SCENARIO).execute(scenario,true);
    }

    @Override
    public void onSaveTimeInterval(ScenarioTimeInterval timeInterval) {

        if (timeInterval.id == 0) {
            scenario.calendar.timeIntervals.add(timeInterval);
            //saveScenario();
        } else {
            for (ScenarioTimeInterval ti : scenario.calendar.timeIntervals) {
                if (ti.id == timeInterval.id) {
                    int itemIndex = scenario.calendar.timeIntervals.indexOf(ti);
                    if (itemIndex != -1) {
                        scenario.calendar.timeIntervals.set(itemIndex, timeInterval);
                    }
                    //saveScenario();
                    return;
                }
            }
        }
    }

    @Override
    public void onDeleteTimeInterval(ScenarioTimeInterval timeInterval) {
        scenario.calendar.timeIntervals.remove(timeInterval);
    }

    @Override
    public void onSaveTrigger(ScenarioTrigger trigger) {

        if (trigger.id == 0) {
            scenario.triggers.add(trigger);
            //saveScenario();
        } else {
            for (ScenarioTrigger trgr : scenario.triggers) {
                if (trgr.id == trigger.id) {
                    int itemIndex = scenario.triggers.indexOf(trgr);
                    if (itemIndex != -1) {
                        scenario.triggers.set(itemIndex, trigger);
                    }
                    //saveScenario();
                    return;
                }
            }
        }
    }

    @Override
    public void onDeleteTrigger(ScenarioTrigger trigger) {
        scenario.triggers.remove(trigger);
    }

    @Override
    public void onSaveProgram(ScenarioProgram program) {

        if (program.id == 0) {
            scenario.programs.add(program);
        } else {
            for (ScenarioProgram prgm : scenario.programs) {
                if (prgm.id == program.id) {
                    int itemIndex = scenario.programs.indexOf(prgm);
                    if (itemIndex != -1) {
                        scenario.programs.set(itemIndex, program);
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void onDeleteProgram(ScenarioProgram program) {

        scenario.programs.remove(program);

    }

    @Override
    public void onSaveProgramAction(ProgramAction action) {

        ScenarioProgram program = scenario.getProgramFromTimerangeId(action.timerangeid);
        ScenarioProgramTimeRange timerange = program.getTimeRangeFromId(action.timerangeid);

        if (timerange != null) {
            if (action.id == 0) {
                timerange.programActionList.add(action);
                //saveScenario();
            } else {
                for (ProgramAction actn : timerange.programActionList) {
                    if (actn.id == action.id) {
                        int itemIndex = timerange.programActionList.indexOf(actn);
                        if (itemIndex != -1) {
                            timerange.programActionList.set(itemIndex, action);
                        }
                        //saveScenario();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onDeleteProgramAction(ProgramAction action) {
        /*ScenarioProgram prgm = scenario.getProgramFromTimerangeId(action.timerangeid);
        ScenarioProgramTimeRange tr = prgm.getTimeRangeFromId(action.timerangeid);
        tr.programActionList.remove(action);*/
    }

    @Override
    public void onSaveProgramTimeRange(ScenarioProgramTimeRange timeRange) {

        ScenarioProgram program = scenario.getProgramFromTimerangeId(timeRange.id);
        ScenarioProgramTimeRange timerange = program.getTimeRangeFromId(timeRange.id);

        if (timerange != null && program != null) {
            if (timerange.id == 0) {
                program.timeRanges.add(timeRange);
                //saveScenario();
            } else {
                for (ScenarioProgramTimeRange trng : program.timeRanges) {
                    if (trng.id == timeRange.id) {
                        int itemIndex = program.timeRanges.indexOf(trng);
                        if (itemIndex != -1) {
                            program.timeRanges.set(itemIndex, timeRange);
                        }
                        //saveScenario();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onDeleteProgramTimeRange(ScenarioProgramTimeRange timeRange) {
        ScenarioProgram prgm = scenario.getProgramFromTimerangeId(timeRange.id);
        prgm.timeRanges.remove(timeRange);
    }
}
