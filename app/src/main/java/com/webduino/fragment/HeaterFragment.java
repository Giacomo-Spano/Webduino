package com.webduino.fragment;

//import android.app.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.webduino.Actuator;
import com.webduino.Actuators;
import com.webduino.CardInfo;
import com.webduino.HeaterActuator;
import com.webduino.HeaterCardInfo;
import com.webduino.HeaterWizardActivity;
import com.webduino.R;
import com.webduino.Sensor;
import com.webduino.SensorAdapter;
import com.webduino.Sensors;
import com.webduino.TemperatureSensor;
import com.webduino.TemperatureSensorCardInfo;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.webduino.HeaterActuator.Command_Manual_Off;
import static com.webduino.HeaterActuator.Command_Manual_On;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterFragment extends Fragment {

    public static final int HEATERWIZARD_MANUAL_OFF = 1;  // The request code
    public static final int HEATERWIZARD_MANUAL_ON = 2;  // The request code

    Button buttonLeave;
    Button buttonManualOn;
    Button buttonAuto;
    Button buttonPrograms;
    TextView textViewReleStatus;
    TextView textViewStatus;
    TextView textViewTarget;

    // Container Activity must implement this interface
    public interface OnHeaterUpdatedListener {
        public void OnHeaterUpdated(HeaterActuator heaterActuator);
    }

    OnHeaterUpdatedListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnHeaterUpdatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        String strtext = getArguments().getString("actuatorid");
        final int id = Integer.valueOf(strtext);

        View v;
        v = inflater.inflate(R.layout.fragment_heater, container, false);

        textViewStatus = (TextView) v.findViewById(R.id.textViewStatus);
        textViewReleStatus = (TextView) v.findViewById(R.id.textViewReleStatus);
        textViewTarget = (TextView) v.findViewById(R.id.textViewTarget);

        buttonLeave = (Button) v.findViewById(R.id.buttonLeave);
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("actuatorid", id);
                bundle.putString("command", Command_Manual_Off);
                intent.putExtras(bundle);
                startActivityForResult(intent, HEATERWIZARD_MANUAL_OFF);
            }
        });

        buttonManualOn = (Button) v.findViewById(R.id.buttonManualOn);
        buttonManualOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("actuatorid", id);
                bundle.putString("command", Command_Manual_On);
                intent.putExtras(bundle);
                startActivityForResult(intent, HEATERWIZARD_MANUAL_ON);
            }
        });
        buttonAuto = (Button) v.findViewById(R.id.buttonAuto);
        buttonPrograms = (Button) v.findViewById(R.id.buttonPrograms);

        HeaterActuator heater = (HeaterActuator) Actuators.getFromId(id);
        update(heater);

        /*final Scene scene = Scene.getSceneForLayout(container,
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
        });*/


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == HEATERWIZARD_MANUAL_OFF || requestCode == HEATERWIZARD_MANUAL_ON) {
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
                    mListener.OnHeaterUpdated(heaterActuator);
                    update(heaterActuator);
                    //updateActuator(heaterActuator);

                }
                // Set the message string in textView
                //textViewMessage.setText("Message from second Activity: " + message);
                // Do something with the contact here (bigger example below)
            }
        }
    }

    private void update(HeaterActuator heater) {

        if (heater == null)
            return;

        textViewStatus.setText(heater.getStatus());
        textViewTarget.setText("" + heater.getTarget());
        if (heater.getReleStatus())
            textViewReleStatus.setText("ACCESO");
        else
            textViewReleStatus.setText("SPENTO");


    }
}
