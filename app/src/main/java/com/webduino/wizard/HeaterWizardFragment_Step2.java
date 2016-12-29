package com.webduino.wizard;

//import android.app.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.webduino.R;
import com.webduino.Sensor;
import com.webduino.Sensors;

import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterWizardFragment_Step2 extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private double temperature = 22;

    public int getSensorId() {
        return sensorId;
    }

    public double getTemperature() {
        return temperature;
    }

    private int sensorId = 0;

    private EditText temperatureEditText;
    private RadioGroup radioGroup;
    private List<RadioButton> radioButtonList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.heaterwizardfragmentstep2, container, false);

        temperatureEditText = (EditText) v.findViewById(R.id.temperatureEditText);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);

        // get and add local sensor radio button
        RadioButton rbn = new RadioButton(this.getActivity());
        rbn.setId(1000 + radioButtonList.size());
        rbn.setText("local");
        rbn.setTag(0);
        rbn.setChecked(true);
        rbn.setOnCheckedChangeListener(this);
        radioButtonList.add(rbn);
        radioGroup.addView(rbn);

        for (Sensor sensor: Sensors.list) {
            rbn = new RadioButton(getActivity());
            rbn.setId(1000 + radioButtonList.size());
            rbn.setText(sensor.getName());
            rbn.setTag(sensor.getId());
            rbn.setOnCheckedChangeListener(this);
            radioButtonList.add(rbn);
            radioGroup.addView(rbn);
        }

        return v;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sensorId = (int) buttonView.getTag();
    }
}
