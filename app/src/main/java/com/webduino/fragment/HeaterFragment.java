package com.webduino.fragment;

//import android.app.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.webduino.elements.Actuators;
import com.webduino.elements.HeaterActuator;
import com.webduino.MainActivity;
import com.webduino.wizard.HeaterWizardActivity;
import com.webduino.R;

import static android.app.Activity.RESULT_OK;
import static com.webduino.elements.HeaterActuator.Command_Manual_End;
import static com.webduino.elements.HeaterActuator.Command_Manual_Off;
import static com.webduino.elements.HeaterActuator.Command_Manual_Auto;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterFragment extends Fragment {

    public static final int HEATERWIZARD_MANUAL_OFF = 1;  // The request code
    public static final int HEATERWIZARD_MANUAL_ON = 2;  // The request code
    public static final int HEATERWIZARD_MANUAL_END = 3;  // The request code

    private Button buttonLeave;
    private Button buttonManualOn;
    private Button buttonAuto;
    private Button buttonPrograms;
    private TextView textViewReleStatus;
    private TextView textViewStatus;
    private TextView textViewProgram;
    private TextView textViewTarget;

    private int actuatorId;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        String strtext = getArguments().getString("actuatorid");
        actuatorId = Integer.valueOf(strtext);

        View v;
        v = inflater.inflate(R.layout.fragment_heater, container, false);

        textViewStatus = (TextView) v.findViewById(R.id.textViewStatus);
        textViewReleStatus = (TextView) v.findViewById(R.id.textViewReleStatus);
        textViewTarget = (TextView) v.findViewById(R.id.textViewTarget);
        textViewProgram = (TextView) v.findViewById(R.id.textViewProgram);

        buttonLeave = (Button) v.findViewById(R.id.buttonLeave);
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("actuatorid", actuatorId);
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
                bundle.putInt("actuatorid", actuatorId);
                bundle.putString("command", Command_Manual_Auto);
                intent.putExtras(bundle);
                startActivityForResult(intent, HEATERWIZARD_MANUAL_ON);
            }
        });
        buttonAuto = (Button) v.findViewById(R.id.buttonAuto);
        buttonAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("actuatorid", actuatorId);
                bundle.putString("command", Command_Manual_End);
                intent.putExtras(bundle);
                startActivityForResult(intent, HEATERWIZARD_MANUAL_END);
            }
        });

        buttonPrograms = (Button) v.findViewById(R.id.buttonPrograms);
        buttonPrograms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("actuatorid", id);
                bundle.putString("command", Command_Manual_On);
                intent.putExtras(bundle);
                startActivityForResult(intent, HEATERWIZARD_MANUAL_ON);*/
            }
        });

        //HeaterActuator heater = (HeaterActuator) Actuators.getFromId(actuatorId);
        update(/*heater*/);

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
                MainActivity a = (MainActivity) getActivity();
                a.getActuatorData();


                /*if (data != null) {
                    Bundle extras = data.getExtras();

                    if (extras != null) {
                        String strJson = extras.getString("actuator");
                        //strJson = extras.getString("WindAlarmProgram");
                        HeaterActuator heaterActuator = new Gson().fromJson(strJson, HeaterActuator.class);
                        mListener.OnHeaterUpdated(heaterActuator);
                        update(heaterActuator);
                        //updateActuator(heaterActuator);

                    }
                }*/
                // Set the message string in textView
                //textViewMessage.setText("Message from second Activity: " + message);
                // Do something with the contact here (bigger example below)
            }
        }
    }

    public void update(/*HeaterActuator heater*/) {

        HeaterActuator heater = (HeaterActuator) Actuators.getFromId(actuatorId);

        if (heater == null)
            return;

        textViewStatus.setText(heater.getStatus());
        textViewTarget.setText("" + heater.getTarget());
        if (heater.getReleStatus())
            textViewReleStatus.setText("ACCESO");
        else
            textViewReleStatus.setText("SPENTO");
        String program = "";
        if (heater.getStatus().equals(HeaterActuator.StatusProgram)) {
            program += "program [" + heater.getActiveProgramId() + "." + heater.getActiveTimeRangeId() + "] ";
            program += heater.getActiveProgramIdName() + "." + heater.getActiveTimeRangeIdName();
            program += " target " + heater.getTarget() + "°C";
            if (heater.getLocalsensor()) {
                program += " local sensor ";
            } else {
                program += " sensore [" + heater.getSensorId() + "]" + heater.getSensorIdName() + "(" + heater.getRemoteTemperature() + ")";
            }
        } else if (heater.getStatus().equals(HeaterActuator.StatusManual)) {
            program += "manual [" + heater.getActiveProgramId() + heater.getActiveTimeRangeId() + "] ";
        } else if (heater.getStatus().equals(HeaterActuator.StatusIdle)) {
            program += "idle";
        }


        if (heater.getStatus().equals(HeaterActuator.StatusProgram) ||
                heater.getStatus().equals(HeaterActuator.StatusManual) ||
                heater.getStatus().equals(HeaterActuator.StatusManualOff)) {
            program += " tempo rimanente " + heater.getRemainig();
            program += "durata " + heater.getDuration();
        }


        textViewProgram.setText(program);

    }
}
