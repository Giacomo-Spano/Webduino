package com.webduino.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.webduino.elements.TimeRange;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.adapters.SimpleItemTouchHelperCallback;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.TimeRangeCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.MultiChoiceOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.scenarios.ScenarioProgram;
import com.webduino.scenarios.ScenarioProgramTimeRange;
import com.webduino.webduinosystems.WebduinoSystem;

import java.util.ArrayList;
import java.util.List;

public class ProgramFragment extends Fragment implements ProgramTimeRangeFragment.OnProgramTimeRangeFragmentInteractionListener {

    ScenarioProgram program = new ScenarioProgram();
    private CardAdapter programTimeRangeAdapter, optionsAdapter;
    OptionCardInfo optionCard_Name, optionCard_Description, optionCard_daysofweek, optionCard_Priority, optionCard_Enabled;
    int webduinosystemid;

    private OnProgramFragmentInteractionListener mListener;

    public ProgramFragment() {
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
        View view = inflater.inflate(R.layout.fragment_program, container, false);

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
        RecyclerView timerangeRecyclerView = (RecyclerView) view.findViewById(R.id.programList);
        timerangeRecyclerView.setHasFixedSize(true);
        timerangeRecyclerView.setLayoutManager(linearLayoutManager);
        programTimeRangeAdapter = new CardAdapter(this, createProgramTimeRangeList());
        timerangeRecyclerView.setAdapter(programTimeRangeAdapter);
        programTimeRangeAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onProgramTimeRangeClick(position, cardInfo);
            }
        });

        // questo serve per abilitare il drag and drop
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(programTimeRangeAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(timerangeRecyclerView);

        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton, getResources().getColor(R.color.colorPrimary), true, getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                program.enabled = optionCard_Enabled.value.getBoolValue();
                program.name = optionCard_Name.value.getStringValue();
                program.description = optionCard_Description.value.getStringValue();
                program.priority = optionCard_Priority.value.getIntValue();
                MultiChoiceOptionCardValue multichioce = (MultiChoiceOptionCardValue) optionCard_daysofweek.value;
                program.monday = multichioce.getValue(0);
                program.tuesday = multichioce.getValue(1);
                program.wednesday = multichioce.getValue(2);
                program.thursday = multichioce.getValue(3);
                program.friday = multichioce.getValue(4);
                program.saturday = multichioce.getValue(5);
                program.sunday = multichioce.getValue(6);
                saveProgram();
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
            deleteProgram();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void saveProgram() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    program = (ScenarioProgram) result;
                    if (mListener != null) {
                        mListener.onSaveProgram(program);
                    }
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        }, requestDataTask.POST_SCENARIOPROGRAM).execute(program, false);
    }

    public void deleteProgram() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                mListener.onDeleteProgram(program);
                getActivity().getFragmentManager().popBackStack();
                ((MainActivity) getActivity()).getScenarioData();
            }
        }, requestDataTask.POST_SCENARIOPROGRAM).execute(program, true);
    }

    private List<CardInfo> createProgramTimeRangeList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (ScenarioProgramTimeRange timerange : program.timeRanges) {
            TimeRangeCardInfo timerangecardinfo = new TimeRangeCardInfo();
            timerangecardinfo.id = timerange.id;
            timerangecardinfo.name = timerange.name;
            timerangecardinfo.timerange = timerange;
            timerangecardinfo.setEnabled(timerange.enabled);
            timerangecardinfo.startTime = timerange.startTime;
            timerangecardinfo.endTime = timerange.endTime;
            result.add(timerangecardinfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi fascia oraria";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    public List<CardInfo> createOptionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();

        optionCard_Enabled = new OptionCardInfo();
        optionCard_Enabled.value = new BooleanOptionCardValue("Stato", program.enabled, "Abilitato", "Disabilitato");
        result.add(optionCard_Enabled);

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue("Nome", program.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue("Descrizione", program.description);
        result.add(optionCard_Description);

        optionCard_Priority = new OptionCardInfo();
        optionCard_Priority.value = new IntegerOptionCardValue("Priorità", program.priority);
        result.add(optionCard_Priority);

        CharSequence[] items = {
                "Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"
        };
        boolean[] itemValues = {
                program.monday, program.tuesday, program.wednesday, program.thursday, program.friday, program.saturday, program.sunday
        };
        optionCard_daysofweek = new OptionCardInfo();
        optionCard_daysofweek.value = new MultiChoiceOptionCardValue("Giorni della settimana", items, itemValues);
        result.add(optionCard_daysofweek);


        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnProgramFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onProgramTimeRangeClick(int position, CardInfo cardInfo) {
        if (cardInfo instanceof TimeRangeCardInfo) {

            showTimeRangeFragment(((TimeRangeCardInfo) cardInfo).timerange);
        } else if (cardInfo instanceof ActionButtonCardInfo) {

            createNewTimeRange();
        }
    }

    private void createNewTimeRange() {
        ScenarioProgramTimeRange timeRange = new ScenarioProgramTimeRange();
        timeRange.programid = program.id;

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

                ScenarioProgramTimeRange timerange = (ScenarioProgramTimeRange) result;
                program.timeRanges.add(timerange);
                updateTimeRangeList();
                showTimeRangeFragment(timerange);
            }
        }, requestDataTask.POST_SCENARIOPROGRAMTIMERANGE).execute(timeRange, false);
    }

    private void updateTimeRangeList() {
        List<CardInfo> list = createProgramTimeRangeList();
        programTimeRangeAdapter.swap(list);
    }

    private void showTimeRangeFragment(ScenarioProgramTimeRange timeRange) {
        ProgramTimeRangeFragment programTimeRangeFragment = new ProgramTimeRangeFragment();
        programTimeRangeFragment.addListener(this);
        if (timeRange != null)
            programTimeRangeFragment.timeRange = timeRange;
        Bundle bundle = new Bundle();
        bundle.putInt("webduinosystemid", webduinosystemid);
        programTimeRangeFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, (Fragment) programTimeRangeFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onSaveProgramTimeRange(ScenarioProgramTimeRange savedtimerange) {

        ScenarioProgramTimeRange timeRange = program.getTimeRangeFromId(savedtimerange.id);

        if (savedtimerange.id == 0) {
            program.timeRanges.add(savedtimerange);
        } else if (timeRange != null) {
            for (ScenarioProgramTimeRange timerange : program.timeRanges) {
                if (timeRange.id == savedtimerange.id) {
                    int itemIndex = program.timeRanges.indexOf(timerange);
                    if (itemIndex != -1) {
                        program.timeRanges.set(itemIndex, savedtimerange);
                    }
                    return;
                }
            }
        }
        mListener.onSaveProgram(program);
    }

    @Override
    public void onDeleteProgramTimeRange(ScenarioProgramTimeRange timeRange) {
        program.timeRanges.remove(timeRange);
    }

    public interface OnProgramFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveProgram(ScenarioProgram program);
        void onDeleteProgram(ScenarioProgram program);
    }
}
