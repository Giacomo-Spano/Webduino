package com.webduino.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.elements.HeaterActuator;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ScenarioCardInfo;
import com.webduino.fragment.cardinfo.WebduinoSystemActuatorCardInfo;
import com.webduino.fragment.cardinfo.WebduinoSystemZoneCardInfo;
import com.webduino.scenarios.Scenario;
import com.webduino.scenarios.Scenarios;
import com.webduino.webduinosystems.WebduinoSystem;
import com.webduino.webduinosystems.WebduinoSystemActuator;
import com.webduino.webduinosystems.WebduinoSystemService;
import com.webduino.webduinosystems.WebduinoSystemZone;

import java.util.ArrayList;
import java.util.List;

public class WebduinoSystemFragment extends Fragment {

    public WebduinoSystem webduinoSystem;

    private CardAdapter scenarioCardAdapter, actuatorCardAdapter, zoneCardAdapter;

    public void update() {
        if (scenarioCardAdapter != null) {
            List<CardInfo> list = createScenarioList();
            scenarioCardAdapter.swap(list);
        }

        if (actuatorCardAdapter != null) {
            List<CardInfo> list = createActuatorList();
            actuatorCardAdapter.swap(list);
        }

        if (zoneCardAdapter != null) {
            List<CardInfo> list = createZoneList();
            zoneCardAdapter.swap(list);
        }
    }

    // Container Activity must implement this interface
    public interface OnWebduinoSystemFragmentListener {
        public void onWebduinoSystemRefresh();
    }

    private List<OnWebduinoSystemFragmentListener> listeners = new ArrayList<>();

    public void addListener(OnWebduinoSystemFragmentListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
        }

        View v = createView(inflater, container, R.layout.fragment_securitysystem);

        //refreshData();
        return v;
    }

    @NonNull
    protected View createView(LayoutInflater inflater, ViewGroup container, int layout) {

        View v = inflater.inflate(layout, container, false);

        // zone list
        RecyclerView zoneRecList = (RecyclerView) v.findViewById(R.id.zoneList);
        zoneRecList.setHasFixedSize(true);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        zoneRecList.setLayoutManager(gridLayoutManager);
        zoneCardAdapter = new CardAdapter(this, createZoneList());
        zoneRecList.setAdapter(zoneCardAdapter);
        zoneCardAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onClickZone(cardInfo);
            }
        });

        //actuator list
        RecyclerView actuatorRecList = (RecyclerView) v.findViewById(R.id.actuatorList);
        zoneRecList.setHasFixedSize(true);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        actuatorRecList.setLayoutManager(gridLayoutManager);
        actuatorCardAdapter = new CardAdapter(this, createActuatorList());
        actuatorRecList.setAdapter(actuatorCardAdapter);
        actuatorCardAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onClickActuator(cardInfo);
            }
        });

        // scenario list
        RecyclerView scenarioRecList = (RecyclerView) v.findViewById(R.id.scenarioList);
        zoneRecList.setHasFixedSize(true);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        scenarioRecList.setLayoutManager(gridLayoutManager);
        scenarioCardAdapter = new CardAdapter(this, createScenarioList());
        scenarioRecList.setAdapter(scenarioCardAdapter);
        scenarioCardAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onClickScenario(cardInfo);
            }
        });

        return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        
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

    public void refreshData() {

    }
    public List<CardInfo> createScenarioList() {

        List<CardInfo> result = new ArrayList<CardInfo>();

        for (Scenario scenario : Scenarios.list) {
            if (scenario.webduinosystemid != webduinoSystem.id)
                continue;
            try {
                CardInfo ci = scenario.getCardInfo();
                result.add(ci);
            } catch (Exception e) {
            }
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 1;
        addButton.name = "Aggiungi Scenario";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.power, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);

        return result;
    }
    public List<CardInfo> createActuatorList() {

        List<CardInfo> result = new ArrayList<CardInfo>();
        for (WebduinoSystemActuator actuator : webduinoSystem.actuators) {

            CardInfo cardinfo;
            if (actuator.actuator != null) {
                cardinfo = actuator.actuator.getCardInfo(this);
                result.add(cardinfo);
            }


            /*if (actuator.actuator instanceof HeaterActuator) {
                cardinfo = actuator.actuator.getCardInfo(this);
                result.add(cardinfo);

            } else {*/

                /*cardinfo = new WebduinoSystemActuatorCardInfo();
                cardinfo.id = actuator.id;
                cardinfo.name = actuator.name;
                cardinfo.actuator = actuator;
                cardinfo.setEnabled(true);
                result.add(cardinfo);*/
            //}
        }

        for (WebduinoSystemService service : webduinoSystem.services) {

            CardInfo cardinfo;

                cardinfo = new WebduinoSystemActuatorCardInfo();
                cardinfo.id = service.id;
                cardinfo.name = service.name;
                //cardinfo.actuator = actuator;
                cardinfo.setEnabled(true);
                result.add(cardinfo);

        }

        return result;
    }

    public List<CardInfo> createZoneList() {

        List<CardInfo> result = new ArrayList<CardInfo>();
        for (WebduinoSystemZone zone : webduinoSystem.zones) {
            WebduinoSystemZoneCardInfo cardinfo = new WebduinoSystemZoneCardInfo();
            cardinfo.id = zone.id;
            cardinfo.name = zone.name;
            cardinfo.zone = zone;
            cardinfo.setEnabled(true);
            result.add(cardinfo);
        }
        return result;
    }

    private void showScenarioFragment(ScenarioCardInfo scenarioCardInfo) {
        Bundle bundle = new Bundle();
        bundle.putInt("scenarioid", scenarioCardInfo.scenario.id);
        bundle.putInt("webduinosystemid", webduinoSystem.id);

        ScenarioFragment scenarioFragment = new ScenarioFragment();
        scenarioFragment.setListener(new ScenarioFragment.OnScenarioFragmentInteractionListener() {
            @Override
            public void onSave(Scenario scenario) {
                for(OnWebduinoSystemFragmentListener listener: listeners) {
                    listener.onWebduinoSystemRefresh();
                }

            }
        });

        scenarioFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, (Fragment) scenarioFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onClickScenario(CardInfo cardInfo) {

        if (cardInfo instanceof ScenarioCardInfo) {

            ScenarioCardInfo scenarioCardInfordInfo = (ScenarioCardInfo) cardInfo;
            showScenarioFragment(scenarioCardInfordInfo);

        } else if (cardInfo instanceof ActionButtonCardInfo) {

            ScenarioCardInfo scenarioCardInfordInfo = new ScenarioCardInfo();
            showScenarioFragment(scenarioCardInfordInfo);
        }

    }

    public void onClickZone(CardInfo cardInfo) {

    }

    public void onClickActuator(CardInfo cardInfo) {

    }


}
