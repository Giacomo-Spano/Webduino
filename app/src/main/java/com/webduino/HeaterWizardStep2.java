package com.webduino;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardStep2 extends WizardStep implements RadioButton.OnCheckedChangeListener {

    @ContextVariable
    private double temperature = 22;
    @ContextVariable
    private int sendorId = 0;

    private EditText temperatureEditText;
    private RadioGroup radioGroup;
    private List<RadioButton> radioButtonList = new ArrayList<>();

    //You must have an empty constructor for every step
    public HeaterWizardStep2() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.heaterwizardstep2, container, false);
        temperatureEditText = (EditText) v.findViewById(R.id.temperatureEditText);

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);

        // get and add local sensor radio button
        RadioButton rbn = new RadioButton(getContext());
        rbn.setId(1000 + radioButtonList.size());
        rbn.setText("local");
        rbn.setTag(0);
        rbn.setChecked(true);
        rbn.setOnCheckedChangeListener(this);
        radioButtonList.add(rbn);
        radioGroup.addView(rbn);

        for (Sensor sensor: Sensors.list) {
            rbn = new RadioButton(getContext());
            rbn.setId(1000 + radioButtonList.size());
            rbn.setText(sensor.name);
            rbn.setTag(sensor.id);
            rbn.setOnCheckedChangeListener(this);
            radioButtonList.add(rbn);
            radioGroup.addView(rbn);
        }

        /*checkBox = (CheckBox) v.findViewById(R.actuatorId.sample_form2_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Notify that the step is completed
                    notifyCompleted();
                }
                else {
                    //Notify that the step is incomplete
                    notifyIncomplete();
                }
            }
        });*/
        return v;
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                //Do nothing...
                break;
        }
    }

    private void bindDataFields() {
        //Do some work
        //...
        //The values of these fields will be automatically stored in the wizard context
        //and will be populated in the next steps only if the same field names are used.
        temperature = Double.valueOf(temperatureEditText.getText().toString());


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        sendorId = (int) compoundButton.getTag();
    }
}