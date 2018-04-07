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
import com.webduino.WebduinoResponse;
import com.webduino.elements.SensorTypes;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.DecimalOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.scenarios.Condition;
import com.webduino.zones.Zone;
import com.webduino.zones.ZoneSensor;
import com.webduino.zones.Zones;

import java.util.ArrayList;
import java.util.List;

public class ConditionFragment extends Fragment {

    Condition condition;
    private CardAdapter optionsAdapter;
    OptionCardInfo optionCard_Zone, optionCard_ZoneSensorId,
                    optionCard_ConditionType, // valore sensore o stato sensore
                    optionCard_ZoneSensorStatus, // stato sensore
                    optionCard_Value, // valore sensore
                    optionCard_ValueOperator; // >, <, ==, etc

    private OnConditionFragmentListener mListener;

    public ConditionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
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

                condition.zoneid = optionCard_ZoneSensorId.value.getIntValue();
                condition.zonesensorid = optionCard_ZoneSensorId.value.getIntValue();
                condition.type = optionCard_ConditionType.value.getStringValue();
                condition.status = optionCard_ZoneSensorStatus.value.getStringValue();
                condition.value = optionCard_Value.value.getDoubleValue();
                condition.valueoperator = optionCard_ValueOperator.value.getStringValue();
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
        }, requestDataTask.POST_CONDITION).execute(condition, false);
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
        final List<CardInfo> result = new ArrayList<CardInfo>();

        // zona
        Zone conditionzone = Zones.getFromId(condition.zoneid);
        CharSequence[] items = new CharSequence[Zones.list.size()];
        String[] itemStringValues;
        int[] itemValues = new int[Zones.list.size()];
        int i = 0;
        for (Zone zone : Zones.list) {
            items[i] = zone.name;
            itemValues[i] = zone.id;
            i++;
        }
        optionCard_Zone = new OptionCardInfo();
        optionCard_Zone.value = new ListOptionCardValue("Zona", condition.zoneid, items, itemValues);
        result.add(optionCard_Zone);
        // zonesensorid
        ZoneSensor conditionZoneSensor = conditionzone.getZoneSensorFromId(condition.zonesensorid);
        items = new CharSequence[conditionzone.zoneSensors.size()];
        itemValues = new int[conditionzone.zoneSensors.size()];
        i = 0;
        for (ZoneSensor zonesensor : conditionzone.zoneSensors) {
            items[i] = zonesensor.name;
            itemValues[i] = zonesensor.id;
            i++;
        }
        optionCard_ZoneSensorId = new OptionCardInfo();
        optionCard_ZoneSensorId.value = new ListOptionCardValue("Sensore", conditionZoneSensor.id, items, itemValues);
        result.add(optionCard_ZoneSensorId);

        // sensor value operator
        if (condition.valueOperatorList != null && condition.valueOperatorList.size() > 0) {
            CharSequence[] cs2 = new CharSequence[condition.valueOperatorList.size()];
            String[] csvalue2 = new String[condition.valueOperatorList.size()];
            i = 0;
            int value = 0;
            for (String operator : condition.valueOperatorList) {
                cs2[i] = operator;
                csvalue2[i] = operator;
                i++;
            }
            optionCard_ValueOperator = new OptionCardInfo();
            optionCard_ValueOperator.value = new ListOptionCardValue("Confronto", value, cs2, csvalue2);
        }
        // sensor value
        optionCard_Value = new OptionCardInfo();
        optionCard_Value.value = new DecimalOptionCardValue("Valore",0.0);
        // status
        com.webduino.elements.Sensor sensor = Sensors.getFromId(conditionZoneSensor.sensorId);
        List<String> statuslist = SensorTypes.getSensorStatusList(sensor);
        int statuscount = 0;
        if (statuslist != null) {
            items = new CharSequence[statuslist.size()];
            itemStringValues = new String[statuslist.size()];
            i = 0;
            for (String status : statuslist) {
                items[i] = (CharSequence) status;
                itemStringValues[i] = status;
                i++;
            }
        } else {
            items = new CharSequence[0];
            itemStringValues = new String[0];
        }
        optionCard_ZoneSensorStatus = new OptionCardInfo();
        optionCard_ZoneSensorStatus.value = new ListOptionCardValue("Stato", 0, items, itemStringValues);
        // value or status
        CharSequence[] cs = {"Valore", "Stato"};
        String[] csvalue = {"value", "status"};
        optionCard_ConditionType = new OptionCardInfo();
        optionCard_ConditionType.value = new ListOptionCardValue("Controlla", 0, cs, csvalue);
        optionCard_ConditionType.value.addListener(new OptionCardValue.OptionCardListener() {
            @Override
            public void onSetValue(Object value) {
                if (value.equals("value")) {
                    result.remove(optionCard_ZoneSensorStatus);
                    result.add(optionCard_ValueOperator);
                    result.add(optionCard_Value);
                } else {
                    result.remove(optionCard_ValueOperator);
                    result.remove(optionCard_Value);
                    result.add(optionCard_ZoneSensorStatus);
                }
            }
        });
        result.add(optionCard_ConditionType);

        return result;
    }
}
