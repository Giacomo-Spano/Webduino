package com.webduino.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.Services;
import com.webduino.WebduinoResponse;
import com.webduino.elements.Sensor;
import com.webduino.elements.SensorTypes;
import com.webduino.elements.Sensors;
import com.webduino.elements.Trigger;
import com.webduino.elements.Triggers;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.DecimalOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.scenarios.Condition;
import com.webduino.webduinosystems.WebduinoSystem;
import com.webduino.webduinosystems.WebduinoSystemZone;
import com.webduino.webduinosystems.WebduinoSystems;
import com.webduino.webduinosystems.services.Service;
import com.webduino.zones.Zone;
import com.webduino.zones.ZoneSensor;
import com.webduino.zones.Zones;

import java.util.ArrayList;
import java.util.List;

public class ConditionFragment extends Fragment {

    Condition condition;
    private CardAdapter optionsAdapter;
    OptionCardInfo optionCard_ZoneId, optionCard_ZoneSensorId, optionCard_TriggerId, optionCard_TriggerStatus,
            optionCard_ConditionType, // valore sensore o stato sensore
            optionCard_ZoneSensorStatus, // stato sensore
            optionCard_Value, // valore sensore
            optionCard_ValueOperator; // >, <, ==, etc
    OptionLoader loader = new OptionLoader();
    private WebduinoSystem webduinoSystem;
    private List<CardInfo> result = new ArrayList<>();

    private OnConditionFragmentListener mListener;

    public ConditionFragment() {
        optionCard_ZoneId = new OptionCardInfo();
        ;
        optionCard_ZoneSensorId = new OptionCardInfo();
        optionCard_TriggerId = new OptionCardInfo();
        optionCard_TriggerStatus = new OptionCardInfo();
        optionCard_ConditionType = new OptionCardInfo();
        optionCard_ZoneSensorStatus = new OptionCardInfo();
        optionCard_Value = new OptionCardInfo();
        optionCard_ValueOperator = new OptionCardInfo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
        int webduinosystemid = getArguments().getInt("webduinosystemid");
        webduinoSystem = WebduinoSystems.getFromId(webduinosystemid);
        condition.timerangeid = getArguments().getInt("timerangeid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_condition, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView optionList = (RecyclerView) view.findViewById(R.id.optionList);
        optionList.setHasFixedSize(true);
        optionList.setLayoutManager(linearLayoutManager);
        optionsAdapter = new CardAdapter(this, createOptionList());
        optionList.setAdapter(optionsAdapter);
        optionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                OptionCardInfo optionCardInfo = (OptionCardInfo) cardInfo;
                optionCardInfo.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        optionsAdapter.notifyDataSetChanged();
                    }
                });
                optionCardInfo.value.showPicker();
            }
        });

        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton, getResources().getColor(R.color.colorPrimary), true, getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (optionCard_ConditionType.value != null)
                    condition.type = optionCard_ConditionType.value.getStringValue();
                if (optionCard_ZoneId.value != null)
                    condition.zoneid = optionCard_ZoneId.value.getIntValue();
                if (optionCard_ZoneSensorId.value != null)
                    condition.zonesensorid = optionCard_ZoneSensorId.value.getIntValue();
                if (optionCard_ZoneSensorStatus.value != null)
                    condition.sensorstatus = optionCard_ZoneSensorStatus.value.getStringValue();
                if (optionCard_TriggerStatus.value != null)
                    condition.triggerstatus = optionCard_TriggerStatus.value.getStringValue();
                if (optionCard_Value.value != null)
                    condition.value = optionCard_Value.value.getDoubleValue();
                if (optionCard_ValueOperator.value != null)
                    condition.valueoperator = optionCard_ValueOperator.value.getStringValue();
                if (optionCard_TriggerId.value != null)
                    condition.triggerid = optionCard_TriggerId.value.getIntValue();
                saveCondition();
            }
        });

        Button cancelbutton = view.findViewById(R.id.cancelButton);
        MainActivity.setImageButton(cancelbutton, getResources().getColor(R.color.colorPrimary), false, getResources().getDrawable(R.drawable.uncheck));
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        ((MainActivity) getActivity()).enableDeleteMenuItem(true);
        return view;
    }

    public void saveCondition() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    condition = (Condition) result;
                    if (mListener != null) {
                        mListener.onSaveCondition(condition);
                    }
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        }, requestDataTask.POST_CONDITION).execute(condition, false);
    }

    public void deleteCondition() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    mListener.onDeleteCondition(condition);
                    getActivity().getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).getScenarioData();
                }
            }
        }, requestDataTask.POST_CONDITION).execute(condition, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteCondition();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnConditionFragmentListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnConditionFragmentListener {
        // TODO: Update argument type and name
        void onSaveCondition(Condition condition);

        void onDeleteCondition(Condition condition);
    }

    public List<CardInfo> createOptionList() {
        result = new ArrayList<CardInfo>();
        loadOptions();
        return result;
    }

    private void loadOptions(/*final List<CardInfo> result*/) {

        if (webduinoSystem == null) return;

        result.clear();

        loader.loadConditionType(optionCard_ConditionType, condition.type);
        result.add(optionCard_ConditionType);
        optionCard_ConditionType.value.addListener(new OptionCardValue.OptionCardListener() {
            @Override
            public void onSetValue(Object value) {
                condition.type = (String) value;
                loadOptions();
            }
        });

        //ActionCommand actionCommand = null;
        if (condition.type.equals(OptionLoader.CONDITION_ZONESENSORVALUE) || condition.type.equals(OptionLoader.CONDITION_ZONESENSORSTATUS)) {

            if (webduinoSystem.zones == null || webduinoSystem.zones.size() == 0)
                return;


            loader.loadZoneId(optionCard_ZoneId, condition.zoneid);
            result.add(optionCard_ZoneId);
            optionCard_ZoneId.value.addListener(new OptionCardValue.OptionCardListener() {
                @Override
                public void onSetValue(Object value) {
                    condition.zoneid = (int) value;
                    loadOptions();
                }
            });
            loader.loadZoneSensorId(optionCard_ZoneSensorId, condition.zoneid, condition.zonesensorid, null);
            if (optionCard_ZoneSensorId.value != null) {
                optionCard_ZoneSensorId.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        condition.zonesensorid = (int) value;
                        loadOptions();
                    }
                });
                result.add(optionCard_ZoneSensorId);
            }


            if (condition.type.equals(OptionLoader.CONDITION_ZONESENSORSTATUS)) {

                Zone zone = Zones.getFromId(condition.zoneid);
                if (zone != null) {
                    ZoneSensor zonesensor = zone.getZoneSensorFromId(condition.zonesensorid);
                    if (zonesensor != null) {
                        loader.loadSensorStatus(optionCard_ZoneSensorStatus, zonesensor.sensorId, condition.sensorstatus);
                        optionCard_ZoneSensorStatus.value.addListener(new OptionCardValue.OptionCardListener() {
                            @Override
                            public void onSetValue(Object value) {
                                condition.sensorstatus = (String) value;
                                loadOptions();
                            }
                        });
                        result.add(optionCard_ZoneSensorStatus);
                    }
                }
            } else if (condition.type.equals(OptionLoader.CONDITION_ZONESENSORVALUE)) {

                loader.loadCompareOperator(optionCard_ValueOperator, condition.valueoperator);
                optionCard_ValueOperator.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        condition.valueoperator = (String) value;
                        loadOptions();
                    }
                });
                result.add(optionCard_ValueOperator);

                loader.loadDecimalValue("Valore", optionCard_Value, condition.value);
                optionCard_Value.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        condition.value = (double) value;
                    }
                });
                result.add(optionCard_Value);
            }


        } else if (condition.type.equals(OptionLoader.CONDITION_TRIGGERSTATUS)) {

            Trigger trigger = Triggers.getFromId(condition.triggerid);
            if (trigger == null)
                condition.triggerid = ((Trigger) Triggers.list.get(0)).id;
            loader.loadTriggerId(optionCard_TriggerId, condition.triggerid);
            if (optionCard_TriggerId.value != null) {
                result.add(optionCard_TriggerId);
                optionCard_TriggerId.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        condition.triggerid = (int) value;
                        loadOptions();
                    }
                });

                loader.loadTriggerStatus(optionCard_TriggerStatus, condition.triggerid, condition.triggerstatus);
                if (optionCard_TriggerStatus.value != null) {
                    optionCard_TriggerStatus.value.addListener(new OptionCardValue.OptionCardListener() {
                        @Override
                        public void onSetValue(Object value) {
                            condition.triggerstatus = (String) value;
                            loadOptions();
                        }
                    });
                    result.add(optionCard_TriggerStatus);
                }
            }
        }
    }
}
