package com.webduino.wizard;

//import android.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.webduino.R;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterWizardFragment_Step1 extends WizardFragment implements RadioGroup.OnCheckedChangeListener {

    public static int always = 1;
    public static int for30Minutes = 2;
    public static int toTime = 3;

    private int duration = 30;

    //OnNextListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.wizard_fragment_heater_step1, container, false);

        RadioGroup radiogroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radiogroup.setOnCheckedChangeListener(this);

        WizardActivity activity = (WizardActivity) getActivity();
        listener = activity;

        return v;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.alwaysRadioButton:
                duration = 30;
                listener.OnNext(always);
                break;
            case R.id.for30MinutesRadioButton:
                duration = 30;
                listener.OnNext(for30Minutes);
                break;
            case R.id.neverRadioButton:
                duration = 30;
                listener.OnNext(toTime);
                break;
        }
    }

    public int getDuration() {
        return duration;
    }
}
