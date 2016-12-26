package com.webduino.fragment;

//import android.app.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.webduino.Actuator;
import com.webduino.Actuators;
import com.webduino.HeaterActuator;
import com.webduino.HeaterCardInfo;
import com.webduino.Sensor;
import com.webduino.SensorAdapter;
import com.webduino.CardInfo;
import com.webduino.R;
import com.webduino.Sensors;
import com.webduino.TemperatureSensor;
import com.webduino.TemperatureSensorCardInfo;
import com.webduino.HeaterWizardActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class SensorsFragment extends Fragment implements SensorAdapter.OnListener {

    public static final int HEATERWIZARD_REQUEST = 1;  // The request code

    private List<CardInfo> list;
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
        /*List<CardInfo>*/
        list = createSensorList();
        sensorAdapter.swap(list);
    }

    public void updateActuator(Actuator actuator) {

        for (CardInfo ci : list) {

            try {
                HeaterCardInfo hci = (HeaterCardInfo) ci;
                if (hci.actuatorId == actuator.getId()) {
                    hci.copyFrom((HeaterActuator) actuator);
                }
                sensorAdapter.swap(list);
                return;
            } catch (ClassCastException e) {

            }
        }


    }

    public List<CardInfo> createSensorList() {


        List<CardInfo> result = new ArrayList<CardInfo>();

        for (Actuator actuator : Actuators.list) {

            try {
                HeaterCardInfo ci = heaterCardInfoFromActuator((HeaterActuator) actuator);
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
        return result;
    }

    @NonNull
    private HeaterCardInfo heaterCardInfoFromActuator(HeaterActuator actuator) {
        HeaterActuator heater = actuator;
        HeaterCardInfo ci = new HeaterCardInfo();
        ci.name = heater.getName();
        ci.actuatorId = heater.getId();
        ci.status = heater.getStatus();
        ci.target = heater.getTarget();
        ci.releStatus = heater.getReleStatus();
        return ci;
    }

    @Override
    public void onHeaterClick(int id) {

        // sostituire con show fragment
        /*Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
        intent.putExtra("actuatorid", id);
        startActivityForResult(intent, HEATERWIZARD_REQUEST);*/

        Bundle bundle = new Bundle();
        bundle.putString("actuatorid", "" + id);

        HeaterFragment fragment = new HeaterFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == HEATERWIZARD_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                // fetch the message String

                Bundle extras = data.getExtras();
                if (extras != null) {
                    String strJson = extras.getString("actuator");
                    //strJson = extras.getString("WindAlarmProgram");
                    HeaterActuator heaterActuator = new Gson().fromJson(strJson, HeaterActuator.class);
                    updateActuator(heaterActuator);

                }
                // Set the message string in textView
                //textViewMessage.setText("Message from second Activity: " + message);
                // Do something with the contact here (bigger example below)
            }
        }
    }
}
