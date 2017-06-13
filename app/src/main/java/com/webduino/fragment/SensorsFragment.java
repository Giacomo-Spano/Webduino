package com.webduino.fragment;

//import android.app.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webduino.elements.Actuator;
import com.webduino.elements.Actuators;
import com.webduino.elements.DoorSensor;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.Sensor;
import com.webduino.R;
import com.webduino.elements.Sensors;
import com.webduino.elements.TemperatureSensor;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.DoorSensorCardInfo;
import com.webduino.fragment.cardinfo.HeaterCardInfo;
import com.webduino.fragment.cardinfo.TemperatureSensorCardInfo;

import java.util.ArrayList;
import java.util.List;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class SensorsFragment extends Fragment implements CardAdapter.OnListener {

    public static final int HEATERWIZARD_REQUEST = 1;  // The request code

    private List<CardInfo> list;
    private CardAdapter cardAdapter;
    private HeaterFragment heaterFragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.fragment_sensors, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);

        cardAdapter = new CardAdapter(this,createSensorList());
        recList.setAdapter(cardAdapter);
        cardAdapter.setListener(this);

        return v;
    }

    public void update() {

        list = createSensorList();
        cardAdapter.swap(list);

        if (heaterFragment != null) {

            heaterFragment.update();
        }
    }

    public void updateActuator(Actuator actuator) {

        for (CardInfo ci : list) {

            try {
                HeaterCardInfo hci = (HeaterCardInfo) ci;
                if (hci.actuatorId == actuator.getId()) {
                    hci.copyFrom((HeaterActuator) actuator);
                }
                cardAdapter.swap(list);
                return;
            } catch (ClassCastException e) {

            }
        }


    }

    public List<CardInfo> createSensorList() {

        List<CardInfo> result = new ArrayList<CardInfo>();

        for (Sensor sensor : Sensors.list) {

            try {
                CardInfo ci = sensor.getCardInfo(this);
                result.add(ci);

                for (Sensor child : sensor.childsensors) {
                    CardInfo ci2 = child.getCardInfo(this);
                    result.add(ci2);
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    @NonNull
    private HeaterCardInfo heaterCardInfoFromActuator(HeaterActuator actuator) {
        HeaterActuator heater = actuator;
        HeaterCardInfo ci = new HeaterCardInfo();
        ci.name = heater.getName();
        ci.actuatorId = heater.getId();
        ci.releStatus = heater.getReleStatus();

        ci.status = heater.getStatus();
        if (ci.status.equals("program")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.auto, null);
            ci.target = heater.getTarget();
            ci.sensorName = heater.getSensorIdName();
            ci.sensorTemperature = heater.getRemoteTemperature();
        } else if (ci.status.equals("idle")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heater, null);
            ci.hideTarget = true;
        } else if (ci.status.equals("manualoff")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.briefcase, null);
            ci.hideTarget = true;
        } else if (ci.status.equals("manual")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.power, null);
            ci.target = heater.getTarget();
            ci.sensorName = heater.getSensorIdName();
            ci.sensorTemperature = heater.getRemoteTemperature();
        } else if (ci.status.equals("idle")) {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heater, null);
            ci.hideTarget = true;
        } else {
            ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heater, null);
            ci.hideTarget = true;
        }
        //ci.target = heater.getTarget();


        if (actuator.getOnLine()) {
            //int temperatureColor = Color.GREEN;
            //ci.setColor(temperatureColor);
            if (actuator.getReleStatus()) {
                ci.setLabelBackgroundColor(Color.GREEN);
                ci.setImageColor(Color.GREEN/*temperatureColor*/);
            } else {
                ci.setLabelBackgroundColor(Color.RED);
                ci.setImageColor(Color.RED/*temperatureColor*/);
            }
            ci.setLabelColor(Color.WHITE);
            ci.setTitleColor(Color.GRAY);
            ci.setEnabled(true);
        } else {
            //ci.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heater, null);
            ci.setEnabled(false);
        }


        return ci;
    }

    @Override
    public void onClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof HeaterCardInfo) {

            HeaterCardInfo heaterCerdInfo = (HeaterCardInfo) cardInfo;
            Bundle bundle = new Bundle();
            bundle.putString("actuatorid", "" + heaterCerdInfo.actuatorId);

            heaterFragment = new HeaterFragment();
            heaterFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, heaterFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
