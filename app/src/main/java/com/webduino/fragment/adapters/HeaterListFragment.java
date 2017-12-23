package com.webduino.fragment.adapters;


import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.Sensors;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.wizard.HeaterWizardActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.webduino.elements.HeaterActuator.Command_Manual;
import static com.webduino.elements.HeaterActuator.Command_Off;

public class HeaterListFragment extends ListFragment implements CardAdapter.OnListener {

    private long spotID;
    //private HeaterData meteoData;

    private int actuatorId;
    private int shieldId;

    public boolean adaptercreated = false;

    ArrayList<HeaterListItem> list = new ArrayList<>();

    protected final int BUTTON_MANUALOFF = 1;
    protected final int BUTTON_MANUAL = 2;
    protected final int BUTTON_AUTO = 3;
    protected final int BUTTON_PROGRAM = 4;
    protected final int BUTTON_SCHEDULE = 5;
    protected final int BUTTON_LOG = 6;

    public static final int HEATERWIZARD_MANUAL_OFF = 1;  // The request code
    public static final int HEATERWIZARD_MANUAL_ON = 2;  // The request code
    public static final int HEATERWIZARD_MANUAL_END = 3;  // The request code

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


    public interface OnMeteoItemListListener {
        void onSpotListChangeSelection(List<Long> list);
    }

    MeteoItemListListener listener;

    public void setListener(MeteoItemListListener listener) {
        this.listener = listener;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyAdapter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setDivider(null);
        getListView().setDividerHeight(0);
        createAdapter();
        refreshData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        /*String strtext = getArguments().getString("shieldid");
        shieldId = Integer.valueOf(strtext);
        strtext = getArguments().getString("id");
        actuatorId = Integer.valueOf(strtext);*/

        /*View v;
        v = inflater.inflate(R.layout.fragment_heater, container, false);*/

        View v = inflater.inflate(R.layout.main, container, false);
        //return root;

        /*textViewStatus = (TextView) v.findViewById(R.id.textViewStatus);
        textViewReleStatus = (TextView) v.findViewById(R.id.textViewReleStatus);
        textViewTarget = (TextView) v.findViewById(R.id.textViewTarget);
        textViewProgram = (TextView) v.findViewById(R.id.textViewProgram);*/
        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);

        cardAdapter = new CardAdapter(this,createActionButtonList());
        recList.setAdapter(cardAdapter);
        cardAdapter.setListener(this);

        refreshData();
        return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String strtext = getArguments().getString("shieldid");
        shieldId = Integer.valueOf(strtext);
        strtext = getArguments().getString("id");
        actuatorId = Integer.valueOf(strtext);

        //refreshData();
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


    public void refreshData() {

        HeaterActuator heater = (HeaterActuator) Sensors.getFromId(actuatorId);

        if (heater == null)
            return;

        if (!adaptercreated)
            return;

        /*List<HeaterListItem>*/ list = new ArrayList<>();
        int count = 0;

        // Heater
        HeaterListItem mi = new HeaterListItem();
        mi.type = 0;
        mi.description = "Stato";
        list.add(mi);
        // id
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Id";
        mi.value = "" + heater.getId();
        list.add(mi);
        // shieldid
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "ShieldId";
        mi.value = "" + heater.getShieldId();
        list.add(mi);
        // name
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Nome";
        mi.value = "" + heater.getName();
        list.add(mi);
        // stato
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Stato";
        mi.value = "" + heater.getStatus();
        list.add(mi);
        // relestatus
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Relè";
        if (heater.getReleStatus())
            mi.value = "ACCESO";
        else
            mi.value = "SPENTO";
        list.add(mi);
        // temperature
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Temperatura";
        mi.value = "" + heater.getRemoteTemperature() + "°C " + heater.getLastTemperatureUpdate();
        list.add(mi);
        // zone
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Zona";
        mi.value = "" + heater.getZoneId();
        list.add(mi);
        // target
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Target";
        mi.value = "" + heater.getTarget() + "°C";
        list.add(mi);
        // last command
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Ultimo comando";
        mi.value = heater.getLastCommandDate();
        list.add(mi);

        // Heater
        mi = new HeaterListItem();
        mi.type = 0;
        mi.description = "Programma";
        list.add(mi);
        // action
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Action";
        mi.value = "" /*+ /*heater.get*/;
        list.add(mi);
        // action
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Fine programma";
        mi.value = heater.getEndDate();
        list.add(mi);
        // action
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Durata";
        mi.value = heater.getDuration();
        list.add(mi);
        // action
        mi = new HeaterListItem();
        mi.type = 1;
        mi.description = "Tempo rimanente";
        mi.value = heater.getRemainig();
        list.add(mi);

        MeteoItemListListener.MeteoItemListArrayAdapter adapter = new MeteoItemListListener.MeteoItemListArrayAdapter(getActivity(), list, listener);
        setListAdapter(adapter);



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
        if (card != null) {
            card.setEnabled(enabled);
            position = cardAdapter.getPositionFromId(button);
            cardAdapter.setCardInfo(position, card);
        }
    }

    /*public void setMeteoData(HeaterData meteoData) {
        //this.meteoData = meteoData;
        refreshData();
    }*/

    public void setSpotId(long id) {
        spotID = id;
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
        bundle.putInt("shieldid", shieldId);
        bundle.putString("command", Command_Off);
        intent.putExtras(bundle);
        startActivityForResult(intent, HEATERWIZARD_MANUAL_END);
    }

    private void buttonManualOnAction() {
        Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("actuatorid", actuatorId);
        bundle.putInt("shieldid", shieldId);
        bundle.putString("command", Command_Manual);
        intent.putExtras(bundle);
        startActivityForResult(intent, HEATERWIZARD_MANUAL_ON);
    }

    private void buttonLeaveAction() {
       /*Intent intent = new Intent(getActivity(), HeaterWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("actuatorid", actuatorId);
        bundle.putInt("shieldid", shieldId);
        bundle.putString("command", Command_Off);
        intent.putExtras(bundle);
        startActivityForResult(intent, HEATERWIZARD_MANUAL_OFF);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == HEATERWIZARD_MANUAL_END || requestCode == HEATERWIZARD_MANUAL_ON) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                // fetch the message String
                MainActivity a = (MainActivity) getActivity();
                a.getSensorData();
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
            //result.add(manualOff);

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
            //result.add(programs);

            ActionButtonCardInfo schedule = new ActionButtonCardInfo();
            schedule.id = BUTTON_SCHEDULE;
            schedule.name = "Programmi";
            schedule.label = " ";
            schedule.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.schedule, null);
            schedule.setColor(Color.BLUE);
            //result.add(schedule);

            ActionButtonCardInfo log = new ActionButtonCardInfo();
            log.id = BUTTON_LOG;
            log.name = "Grafico";
            log.label = " ";
            log.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.graph, null);
            log.setColor(Color.BLUE);
            //result.add(log);

        } catch (Exception e) {

        }

        return result;
    }
}
