package com.webduino.wizard;

//import android.app.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.webduino.R;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.fragment.NumberPickerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterWizardFragment_Temperature extends Fragment implements CompoundButton.OnCheckedChangeListener {


    private double temperature = 0;
    private boolean remoteSensor;
    private int sensorId = 0;
    private EditText temperatureEditText;
    private RadioGroup radioGroup;
    //private List<RadioButton> radioButtonList = new ArrayList<>();
    private HashMap<Integer,RadioButton> hashMap = new HashMap();

    protected Handler numberHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle != null) {
                Double value = bundle.getDouble("value");
                String tag = bundle.getString("tag");
                if (tag.equals("temperature")) {
                    temperature = value;
                    //temperatureEditText.setText(value.toString());
                    update();
                }
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.wizard_fragment_heater_temperature, container, false);

        temperatureEditText = (EditText) v.findViewById(R.id.temperatureEditText);
        temperatureEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(temperature,1,100,0,"imposta temperatura", "temperature",numberHandler);
            }
        });

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);

        // get and add local sensor radio button
        //RadioButton rbn = (RadioButton) v.findViewById(R.id.radioButton);
        int id = R.id.radioGroup + 1000;
        RadioButton rbn = new RadioButton(getActivity());
        rbn.setText("local");
        rbn.setTag(0);
        rbn.setId(id);
        hashMap.put(0,rbn);
        rbn.setOnCheckedChangeListener(this);
        radioGroup.addView(rbn);
        for (Sensor sensor: Sensors.list) {
            rbn = new RadioButton(getActivity());
            rbn.setId(++id);
            rbn.setText(sensor.getName());
            rbn.setTag(sensor.getId());
            rbn.setOnCheckedChangeListener(this);
            radioGroup.addView(rbn);
            hashMap.put(sensor.getId(),rbn);
        }
        update();
        return v;
    }

    public void update() {

        if (temperatureEditText != null)
            temperatureEditText.setText("" + temperature);
        if (!remoteSensor) {
            RadioButton rb = hashMap.get(0);
            if (rb != null)
            rb.setChecked(true);
        } else {
            RadioButton rb = hashMap.get(sensorId);
            if (rb != null)
                rb.setChecked(true);
        }
    }

    public void init(double temperature, boolean remote, int sensorId) {
        this.temperature = temperature;
        this.remoteSensor = remote;
        this.sensorId = sensorId;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sensorId = (int) buttonView.getTag();
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean getRemoteSensor() {
        return remoteSensor;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void showNumberPickerDialog(double value, int decimals, double max, double min, String title, String tag, Handler mHandler) {

        Bundle b = new Bundle();
        b.putDouble("value", value);
        b.putInt("decimals", decimals);
        b.putDouble("max", max);
        b.putDouble("min", min);
        b.putString("title", title);
        b.putString("tag", tag);

        NumberPickerFragment numberPickerFragment = new NumberPickerFragment();
        numberPickerFragment.setNumberHandler(mHandler/*numberHandler*/);
        numberPickerFragment.setArguments(b);
        FragmentManager fm = WizardActivity.activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(numberPickerFragment, "numbr_picker");
        ft.commit();
    }
}
