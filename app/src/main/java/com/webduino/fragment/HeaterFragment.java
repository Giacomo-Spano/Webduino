package com.webduino.fragment;

//import android.app.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.webduino.elements.Actuators;
import com.webduino.elements.HeaterActuator;
import com.webduino.MainActivity;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.wizard.HeaterWizardActivity;
import com.webduino.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.webduino.elements.HeaterActuator.Command_Manual_End;
import static com.webduino.elements.HeaterActuator.Command_Manual_Off;
import static com.webduino.elements.HeaterActuator.Command_Manual_Auto;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterFragment extends Fragment implements CardAdapter.OnListener {

    public static final int HEATERWIZARD_MANUAL_OFF = 1;  // The request code
    public static final int HEATERWIZARD_MANUAL_ON = 2;  // The request code
    public static final int HEATERWIZARD_MANUAL_END = 3;  // The request code

    protected final int BUTTON_MANUALOFF = 1;
    protected final int BUTTON_MANUAL = 2;
    protected final int BUTTON_AUTO = 3;
    protected final int BUTTON_PROGRAM = 4;
    protected final int BUTTON_SCHEDULE = 5;
    protected final int BUTTON_LOG = 6;

    private TextView textViewReleStatus;
    private TextView textViewStatus;
    private TextView textViewProgram;
    private TextView textViewTarget;

    private int actuatorId;
    private CardAdapter cardAdapter;

    @Override
    public void onClick(int position, CardInfo cardInfo) {

        switch (cardInfo.id) {
            case BUTTON_MANUALOFF:
                buttonLeaveAction();
                break;
            case BUTTON_MANUAL:
                buttonManualOnAction();
                break;
            case BUTTON_AUTO:
                buttonAutoAction();
                break;
            case BUTTON_PROGRAM:
                buttonProgramsAction();
                break;
            case BUTTON_SCHEDULE:
                buttonScheduleAction();
                break;
            case BUTTON_LOG:
                buttonLogAction();
                break;
        }
    }

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

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);

        cardAdapter = new CardAdapter(this,createActionButtonList());
        recList.setAdapter(cardAdapter);
        cardAdapter.setListener(this);

        update();
        return v;
    }

    private void buttonLogAction() {
        MainActivity ma = (MainActivity) getActivity();
        ma.showHistory(actuatorId);
    }

    private void buttonScheduleAction() {
        MainActivity ma = (MainActivity) getActivity();
        ma.showSchedule();
    }

    private void buttonProgramsAction() {
        MainActivity ma = (MainActivity) getActivity();
        ma.showPrograms();
    }

    private void buttonAutoAction() {
        Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("actuatorid", actuatorId);
        bundle.putString("command", Command_Manual_End);
        intent.putExtras(bundle);
        startActivityForResult(intent, HEATERWIZARD_MANUAL_END);
    }

    private void buttonManualOnAction() {
        Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("actuatorid", actuatorId);
        bundle.putString("command", Command_Manual_Auto);
        intent.putExtras(bundle);
        startActivityForResult(intent, HEATERWIZARD_MANUAL_ON);
    }

    private void buttonLeaveAction() {
        Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("actuatorid", actuatorId);
        bundle.putString("command", Command_Manual_Off);
        intent.putExtras(bundle);
        startActivityForResult(intent, HEATERWIZARD_MANUAL_OFF);
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
                //a.getActuatorData();


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

    public void update() {

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

            int remainingMinutes = heater.getRemainig() / 60;
            int hour = remainingMinutes / 60;
            int minute = remainingMinutes % 60;
            String str;
            str = String.format("%2d:%2d", hour, minute);
            program += "\ntempo rimanente " + str;
            program += " (" + heater.getRemainig() + ")";

            remainingMinutes = heater.getDuration() / 60;
            hour = remainingMinutes / 60;
            minute = remainingMinutes % 60;
            str = String.format("%2d:%2d", hour, minute);
            program += "\ndurata " + str;
            program += " (" + heater.getDuration() + ")";
        }


        textViewProgram.setText(program);

        // update action buttons
        if(heater.getStatus().equals(HeaterActuator.StatusManualOff)) {
            enableCard(BUTTON_MANUALOFF, true);
            enableCard(BUTTON_MANUAL, false);
            enableCard(BUTTON_AUTO, false);
        } else if (heater.getStatus().equals(HeaterActuator.StatusManual)) {
            enableCard(BUTTON_MANUALOFF, false);
            enableCard(BUTTON_MANUAL, true);
            enableCard(BUTTON_AUTO, false);
        } else {
            enableCard(BUTTON_MANUALOFF, false);
            enableCard(BUTTON_MANUAL, false);
            enableCard(BUTTON_AUTO, true);
        }
        enableCard(BUTTON_SCHEDULE, true);
        enableCard(BUTTON_PROGRAM, true);
        enableCard(BUTTON_LOG, true);
        cardAdapter.notifyDataSetChanged();

    }

    private void enableCard(int button, boolean enabled) {
        CardInfo card;
        int position;
        card = cardAdapter.getCardInfoFromId(button);
        card.setEnabled(enabled);
        position = cardAdapter.getPositionFromId(button);
        cardAdapter.setCardInfo(position,card);
    }

    public List<CardInfo> createActionButtonList() {

        List<CardInfo> result = new ArrayList<CardInfo>();

        try {
            ActionButtonCardInfo manualOff = new ActionButtonCardInfo();
            manualOff.id = BUTTON_MANUALOFF;
            manualOff.name = "Vacanza";
            manualOff.label = "In vacanza";
            manualOff.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.briefcase, null);
            manualOff.setColor(Color.BLUE);
            /*manualOff.labelBackgroundColor = Color.BLUE;
            manualOff.labelColor = Color.WHITE;
            manualOff.titleColor = Color.BLUE;
            manualOff.imageColor = Color.BLUE;*/
            manualOff.setEnabled(true);
            result.add(manualOff);

            ActionButtonCardInfo manual = new ActionButtonCardInfo();
            manual.id = BUTTON_MANUAL;
            manual.name = "Manuale";
            manual.label = "23.1°C";
            manual.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.power, null);
            manual.setColor(Color.BLUE);
            result.add(manual);

            ActionButtonCardInfo auto = new ActionButtonCardInfo();
            auto.id = BUTTON_AUTO;
            auto.name = "Auto";
            auto.label = "Programma";
            auto.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.auto, null);
            auto.setColor(Color.BLUE);
            result.add(auto);

            ActionButtonCardInfo programs = new ActionButtonCardInfo();
            programs.id = BUTTON_PROGRAM;
            programs.name = "Calendario";
            programs.label = "";
            programs.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
            programs.setColor(Color.BLUE);
            result.add(programs);

            ActionButtonCardInfo schedule = new ActionButtonCardInfo();
            schedule.id = BUTTON_SCHEDULE;
            schedule.name = "Programmi";
            schedule.label = " ";
            schedule.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.schedule, null);
            schedule.setColor(Color.BLUE);
            result.add(schedule);

            ActionButtonCardInfo log = new ActionButtonCardInfo();
            log.id = BUTTON_LOG;
            log.name = "Grafico";
            log.label = " ";
            log.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.graph, null);
            log.setColor(Color.BLUE);
            result.add(log);

        } catch (Exception e) {

        }

        return result;
    }
}
