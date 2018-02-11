package com.webduino.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webduino.R;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.adapters.HeaterDataRowItem;
import com.webduino.fragment.adapters.HeaterListListener;
import com.webduino.fragment.cardinfo.TimeIntervalCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ProgramCardInfo;
import com.webduino.fragment.cardinfo.TriggerCardInfo;
import com.webduino.scenarios.Scenario;
import com.webduino.scenarios.ScenarioProgram;
import com.webduino.scenarios.ScenarioTimeInterval;
import com.webduino.scenarios.ScenarioTrigger;
import com.webduino.scenarios.Scenarios;

import java.util.ArrayList;
import java.util.List;

public class ScenarioFragment extends Fragment implements CardAdapter.OnListener,
        TimeIntervalFragment.OnTimeIntervalFragmentInteractionListener {

    private Scenario scenario;
    public boolean adaptercreated = false;

    ArrayList<HeaterDataRowItem> list = new ArrayList<>();

    protected final int BUTTON_MANUALOFF = 1;
    protected final int BUTTON_MANUAL = 2;
    protected final int BUTTON_AUTO = 3;
    protected final int BUTTON_DETAILS = 4;

    private CardAdapter cardAdapter;

    HeaterListListener listener;

    public void setListener(HeaterListListener listener) {
        this.listener = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //refreshData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }
        View v = inflater.inflate(R.layout.fragment_scenario, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView calendarList = (RecyclerView) v.findViewById(R.id.calendarList);
        calendarList.setHasFixedSize(true);
        calendarList.setLayoutManager(linearLayoutManager);
        cardAdapter = new CardAdapter(this, createCalendarList());
        calendarList.setAdapter(cardAdapter);
        cardAdapter.setListener(this);

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView triggerList = (RecyclerView) v.findViewById(R.id.triggerList);
        triggerList.setHasFixedSize(true);
        triggerList.setLayoutManager(linearLayoutManager);
        cardAdapter = new CardAdapter(this, createTriggerList());
        triggerList.setAdapter(cardAdapter);
        cardAdapter.setListener(this);
        //triggerList.setNestedScrollingEnabled(false);

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView programList = (RecyclerView) v.findViewById(R.id.programList);
        programList.setHasFixedSize(true);
        programList.setLayoutManager(linearLayoutManager);
        cardAdapter = new CardAdapter(this, createProgramList());
        programList.setAdapter(cardAdapter);
        cardAdapter.setListener(this);

        return v;
    }

    @Override
    public void onClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof TimeIntervalCardInfo) {

            TimeIntervalCardInfo timeIntervalCardInfo = (TimeIntervalCardInfo) cardInfo;
            TimeIntervalFragment timeIntervalFragment = new TimeIntervalFragment();
            timeIntervalFragment.addListener(this);
            ScenarioTimeInterval timeInterval = scenario.calendar.getTimeIntervalFromId(timeIntervalCardInfo.id);
            if (timeInterval != null)
                timeIntervalFragment.timeInterval = timeInterval;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment ) timeIntervalFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public List<CardInfo> createCalendarList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for(ScenarioTimeInterval timeInterval:scenario.calendar.timeintervals) {
            TimeIntervalCardInfo timeintervalcardinfo = new TimeIntervalCardInfo();
            timeintervalcardinfo.id = timeInterval.id;
            timeintervalcardinfo.name = timeInterval.name;
            //manualOff.label = "Manual Off";
            //manualOff.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.briefcase, null);
            //manualOff.setColor(Color.BLUE);
            /*manualOff.labelBackgroundColor = Color.BLUE;
            manualOff.labelColor = Color.WHITE;
            manualOff.titleColor = Color.BLUE;
            manualOff.imageColor = Color.BLUE;*/
            timeintervalcardinfo.setEnabled(timeInterval.enabled);
            result.add(timeintervalcardinfo);
        }
        return result;
    }

    public List<CardInfo> createTriggerList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for(ScenarioTrigger trigger:scenario.triggers) {
            TriggerCardInfo triggercardinfo = new TriggerCardInfo();
            triggercardinfo.id = trigger.id;
            triggercardinfo.name = trigger.name;
            //manualOff.label = "Manual Off";
            //manualOff.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.briefcase, null);
            //manualOff.setColor(Color.BLUE);
            /*manualOff.labelBackgroundColor = Color.BLUE;
            manualOff.labelColor = Color.WHITE;
            manualOff.titleColor = Color.BLUE;
            manualOff.imageColor = Color.BLUE;*/
            triggercardinfo.setEnabled(trigger.enabled);
            result.add(triggercardinfo);
        }
        return result;
    }

    public List<CardInfo> createProgramList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for(ScenarioProgram program:scenario.programs) {
            ProgramCardInfo programCardInfo = new ProgramCardInfo();
            programCardInfo.id = program.id;
            programCardInfo.name = program.name;
            //manualOff.label = "Manual Off";
            //manualOff.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.briefcase, null);
            //manualOff.setColor(Color.BLUE);
            /*manualOff.labelBackgroundColor = Color.BLUE;
            manualOff.labelColor = Color.WHITE;
            manualOff.titleColor = Color.BLUE;
            manualOff.imageColor = Color.BLUE;*/
            programCardInfo.setEnabled(program.enabled);
            result.add(programCardInfo);
        }
        return result;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //String strtext = getArguments().getString("shieldid");
        //shieldId = Integer.valueOf(strtext);
        String strtext = getArguments().getString("id");
        int id = Integer.valueOf(strtext);
        scenario = Scenarios.getFromId(id);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void createAdapter() {
        adaptercreated = true;
    }

    public void destroyAdapter() {
        adaptercreated = false;
    }

    @Override
    public void onSaveTimeInterval(ScenarioTimeInterval timeInterval) {
        for (ScenarioTimeInterval ti:scenario.calendar.timeintervals) {
            if (ti.id == timeInterval.id) {
                scenario.calendar.timeintervals.remove(ti);
                scenario.calendar.timeintervals.add(timeInterval);
                return;
            }
        }
    }
}
