package com.webduino.fragment;


import android.app.Fragment;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.webduino.HeaterActivity;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.WebduinoResponse;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.ProgramActionTypes;
import com.webduino.elements.Sensors;
import com.webduino.elements.Shield;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.adapters.HeaterDataRowItem;
import com.webduino.fragment.adapters.HeaterListListener;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.wizard.HeaterWizardActivity;
import com.webduino.zones.Zone;
import com.webduino.zones.Zones;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.webduino.elements.HeaterActuator.Command_Manual;
import static com.webduino.elements.HeaterActuator.Command_Off;

public class HeaterFragment extends Fragment implements CardAdapter.OnListener {

    private int actuatorId;
    private int shieldId;
    private int zoneid = 0;
    CharSequence[] zoneIdList = new CharSequence[ProgramActionTypes.list.size()];
    int[] zoneIdValues = new int[ProgramActionTypes.list.size()];

    public boolean adaptercreated = false;

    ArrayList<HeaterDataRowItem> list = new ArrayList<>();

    protected final int BUTTON_MANUALOFF = 1;
    protected final int BUTTON_MANUAL = 2;
    protected final int BUTTON_AUTO = 3;
    protected final int BUTTON_DETAILS = 4;

    public static final int HEATERWIZARD_MANUAL_OFF = 1;  // The request code
    public static final int HEATERWIZARD_MANUAL_ON = 2;  // The request code
    public static final int HEATERWIZARD_MANUAL_END = 3;  // The request code

    private CardAdapter cardAdapter;
    private TextView targetTextView, zoneTextView, statusTextView;
    Button okButton, cancelButton;
    private double target = 0.0;

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
            case BUTTON_DETAILS:
                buttonDetailsAction();
                break;
        }
    }


    HeaterListListener listener;

    public void setListener(HeaterListListener listener) {
        this.listener = listener;
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
        View v = inflater.inflate(R.layout.fragment_heater, container, false);
        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        targetTextView = (TextView) v.findViewById(R.id.targetTextView);
        zoneTextView = (TextView) v.findViewById(R.id.zoneTextView);
        statusTextView = (TextView) v.findViewById(R.id.statusTextView);


        // zone

        // zona
        zoneIdList = new CharSequence[Zones.list.size()];
        zoneIdValues = new int[Zones.list.size()];
        int i = 0;
        for (Zone zone : Zones.list) {
            zoneIdList[i] = zone.name;
            zoneIdValues[i] = zone.id;
            i++;
        }
        HeaterActuator heater = (HeaterActuator) Sensors.getFromId(actuatorId);

        if (heater != null)
            zoneid = heater.getZoneId();
        if (zoneid == 0 && zoneIdList.length > 0)
            zoneid = zoneIdValues[0];

        zoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOptionCardValue value = new ListOptionCardValue("Zona", zoneid, zoneIdList, zoneIdValues);
                value.showPicker();
                value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        zoneid = (int) value;
                        refreshData();
                    }
                });
            }
        });




        okButton = (Button) v.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final HeaterActuator heater = (HeaterActuator) Sensors.getFromId(actuatorId);
                String command = Command_Manual;
                double temperature = getTarget();
                //int zoneId = heater.getZoneId();
                int duration = 0;
                Date endtime = new Date();//null;//new LocalTime();
                boolean nexttimerange = true;
                new requestDataTask(getActivity(), /*getAsyncResponse()*/new WebduinoResponse() {
                    @Override
                    public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                        heater.fromJson((JSONObject)result);
                        refreshData();
                    }
                }, requestDataTask.POST_ACTUATOR_COMMAND).execute(shieldId, actuatorId, command, duration, endtime, nexttimerange, temperature, zoneid);

                cancelButton.setVisibility(View.INVISIBLE);
                okButton.setVisibility(View.INVISIBLE);
            }
        });
        cancelButton = (Button) v.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelButton.setVisibility(View.INVISIBLE);
                okButton.setVisibility(View.INVISIBLE);
            }
        });

        cancelButton.setVisibility(View.INVISIBLE);
        okButton.setVisibility(View.INVISIBLE);

        ImageButton increaseButton = (ImageButton) v.findViewById(R.id.increaseButton);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double current = (int)((getTarget()+0.2) * 100 + 0.5) / 100.0;
                setTarget(current);
                cancelButton.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.VISIBLE);
            }
        });
        ImageButton decreaseButton = (ImageButton) v.findViewById(R.id.decreaseButton);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double current = (int)((getTarget()-0.2) * 100 + 0.5) / 100.0;
                setTarget(current);
                cancelButton.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.VISIBLE);
            }
        });




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


    private WebduinoResponse getAsyncResponse() {
        return new WebduinoResponse() {

            @Override
            public void processFinishRegister(long shieldId, boolean error, String errorMessage) {
            }

            @Override
            public void processFinishObjectList(List<Object> objectList, int requestType, boolean error, String errorMessage) {
            }

        };
    }

    public void refreshData() {

        HeaterActuator heater = (HeaterActuator) Sensors.getFromId(actuatorId);
        target = heater.getTarget();

        if (heater == null)
            return;

        String status = "";
        if ( heater.getReleStatus()) {
            status = "Acceso";
            targetTextView.setTextColor(Color.GREEN);
        } else {
            status = "Spanto";
            targetTextView.setTextColor(Color.RED);
        }
        statusTextView.setText(heater.getStatus() + " - " + status);

        setTarget(target);

        zoneTextView.setText("zona " + zoneid + " " + heater.getRemoteTemperature() +" °C");

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
        enableCard(BUTTON_DETAILS, true);
        cardAdapter.notifyDataSetChanged();

    }

    private void setTarget(double target) {
        this.target = target;
        targetTextView.setText(""+this.target+"°C");
    }

    private double getTarget() {
        return target;
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

    private void buttonLogAction() {
        MainActivity ma = (MainActivity) getActivity();
        ma.showHistory(actuatorId);
    }

    private void buttonDetailsAction() {
        Intent intent = new Intent(getActivity(), HeaterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("actuatorid", actuatorId);
        bundle.putInt("shieldid", shieldId);
        bundle.putString("command", Command_Off);
        intent.putExtras(bundle);
        startActivityForResult(intent, HEATERWIZARD_MANUAL_END);
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
                a.getSensorData(true);
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
            manualOff.name = "Off";
            manualOff.label = "Manual Off";
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

            ActionButtonCardInfo details = new ActionButtonCardInfo();
            details.id = BUTTON_DETAILS;
            details.name = "Dettagli";
            details.label = " ";
            details.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.graph, null);
            details.setColor(Color.BLUE);
            result.add(details);

        } catch (Exception e) {

        }

        return result;
    }
}
