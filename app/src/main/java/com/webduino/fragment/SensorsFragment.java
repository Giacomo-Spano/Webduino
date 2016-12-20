package com.webduino.fragment;

//import android.app.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webduino.Actuator;
import com.webduino.Actuators;
import com.webduino.AsyncRequestDataResponse;
import com.webduino.HeaterActuator;
import com.webduino.HeaterCardInfo;
import com.webduino.Sensor;
import com.webduino.SensorAdapter;
import com.webduino.CardInfo;
import com.webduino.R;
import com.webduino.Sensors;
import com.webduino.TemperatureSensor;
import com.webduino.TemperatureSensorCardInfo;
import com.webduino.requestDataTask;

import java.util.ArrayList;
import java.util.List;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class SensorsFragment extends Fragment implements SensorAdapter.OnListener {

    private SensorAdapter sensorAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.fragment_sensors, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);

        /*LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());//// cambiato
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);*/

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);


        sensorAdapter = new SensorAdapter(createSensorList());
        recList.setAdapter(sensorAdapter);

        sensorAdapter.setListener(this);

        return v;
    }

    public void update() {
        List<CardInfo> list = createSensorList();
        sensorAdapter.swap(list);
    }

    public List<CardInfo> createSensorList() {


        List<CardInfo> result = new ArrayList<CardInfo>();

        for (Actuator actuator : Actuators.list) {

            try {
                HeaterActuator heater = (HeaterActuator) actuator;


                HeaterCardInfo ci = new HeaterCardInfo();
                ci.name = heater.getName();
                ci.status = heater.getStatus();
                ci.target = heater.getTarget();
                ci.releStatus = heater.getReleStatus();

                result.add(ci);
            } catch (Exception e) {

            }
        }

        for (Sensor sensor : Sensors.list) {

            try {
                TemperatureSensor ts = (TemperatureSensor) sensor;


                TemperatureSensorCardInfo ci = new TemperatureSensorCardInfo();
                ci.name = ts.getName();
                ci.temperature = ts.getTemperature();
                ci.target = 0.0;
                result.add(ci);
            } catch (Exception e) {

            }
        }
        /*TemperatureSensorCardInfo ci = new TemperatureSensorCardInfo();
        ci.name = "Soggiorno";
        ci.temperature = 20.1;
        ci.target = 21.0;
        result.add(ci);

        ci = new TemperatureSensorCardInfo();
        ci.name = "Camera";
        ci.temperature = 20.1;
        ci.target = 21.0;
        result.add(ci);

        ci = new TemperatureSensorCardInfo();
        ci.name = "Studio";
        ci.temperature = 20.1;
        ci.target = 21.0;
        result.add(ci);

        HeaterCardInfo hci = new HeaterCardInfo();
        hci.name = "Riscaldamento";
        hci.target = 20.1;
        hci.status = "acceso";
        result.add(hci);*/

        return result;
    }

    @Override
    public void onHeaterClick(int id) {

        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = getFragmentManager();
        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();

        HeaterFragment heaterFragment = new HeaterFragment();
        ft.replace(R.id.content_frame, heaterFragment);

        ft.addToBackStack(null);
        // Committing the transaction
        ft.commit();

    }
}
