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
import android.view.View;
import android.view.ViewGroup;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.elements.HeaterActuator;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.HeaterCardInfo;
import com.webduino.fragment.cardinfo.ScenarioCardInfo;
import com.webduino.scenarios.Scenario;
import com.webduino.scenarios.Scenarios;

import java.util.ArrayList;
import java.util.List;

public class ScenariosFragment extends Fragment implements CardAdapter.OnListener {

    private List<CardInfo> list;
    private CardAdapter cardAdapter;
    private ScenarioFragment scenarioFragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
        }

        View v;
        v = inflater.inflate(R.layout.fragment_scenarios, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);

        cardAdapter = new CardAdapter(this, createScenarioList());
        recList.setAdapter(cardAdapter);
        cardAdapter.setListener(this);



        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity)getActivity()).enableDeleteMenuItem(false);
    }

    public void update() {

        list = createScenarioList();
        cardAdapter.swap(list);

        if (scenarioFragment != null) {
            //scenarioFragment.refreshData();
        }
    }

    public List<CardInfo> createScenarioList() {

        List<CardInfo> result = new ArrayList<CardInfo>();

        for (Scenario scenario : Scenarios.list) {
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

    @Override
    public void onClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof ScenarioCardInfo) {

            ScenarioCardInfo scenarioCardInfordInfo = (ScenarioCardInfo) cardInfo;
            showScenarioFragment(scenarioCardInfordInfo);

        } else if (cardInfo instanceof ActionButtonCardInfo) {

            ScenarioCardInfo scenarioCardInfordInfo = new ScenarioCardInfo();
            showScenarioFragment(scenarioCardInfordInfo);
        }
    }

    private void showScenarioFragment(ScenarioCardInfo scenarioCardInfo) {
        Bundle bundle = new Bundle();
        bundle.putString("id", "" + scenarioCardInfo.id);
        scenarioFragment = new ScenarioFragment();
        scenarioFragment.setListener(new ScenarioFragment.OnScenarioFragmentInteractionListener() {
            @Override
            public void onSave(Scenario scenario) {

            }
        });

        scenarioFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, (Fragment) scenarioFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
