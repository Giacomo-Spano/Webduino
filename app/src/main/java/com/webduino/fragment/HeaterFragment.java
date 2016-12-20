package com.webduino.fragment;

//import android.app.Fragment;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.webduino.Actuator;
import com.webduino.Actuators;
import com.webduino.CardInfo;
import com.webduino.HeaterActuator;
import com.webduino.HeaterCardInfo;
import com.webduino.R;
import com.webduino.Sensor;
import com.webduino.SensorAdapter;
import com.webduino.Sensors;
import com.webduino.TemperatureSensor;
import com.webduino.TemperatureSensorCardInfo;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterFragment extends Fragment {

    Button buttonOn30Minutes;
    Button buttonOn60Minutes;
    Button buttonOnHoursAndMinutes;
    Button buttonOnToDate;
    Button buttonOff30Minutes;
    Button buttonOff60Minutes;
    Button buttonOffHoursAndMinutes;
    Button buttonOffToDate;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.fragment_heater, container, false);

        buttonOn30Minutes = (Button) v.findViewById(R.id.buttonOn30minutes);
        buttonOn60Minutes = (Button) v.findViewById(R.id.buttonOn60minutes);
        buttonOnHoursAndMinutes = (Button) v.findViewById(R.id.buttonOnHourAndminutes);
        buttonOnToDate = (Button) v.findViewById(R.id.buttonOnToDate);
        buttonOff30Minutes = (Button) v.findViewById(R.id.buttonOff30minutes);
        buttonOff60Minutes = (Button) v.findViewById(R.id.buttonOff60Minutes);
        buttonOffHoursAndMinutes = (Button) v.findViewById(R.id.buttonOffHourAndMinutes);
        buttonOffToDate = (Button) v.findViewById(R.id.buttonOffToDate);


        final Scene scene = Scene.getSceneForLayout(container,
                R.layout.fragment_transition_scene_1, getActivity());
        Button goButton = (Button)v.findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TransitionManager.go(scene);
                View parent = (View)buttonOn30Minutes.getParent();
                int parentHeight = parent.getHeight();

                float height = buttonOn30Minutes.getHeight();




                int width = buttonOn30Minutes.getWidth();
                //buttonOn30Minutes.animate().translationX(-width/2);
                buttonOn30Minutes.animate().translationY(parentHeight);
            }
        });


        return v;
    }
}
