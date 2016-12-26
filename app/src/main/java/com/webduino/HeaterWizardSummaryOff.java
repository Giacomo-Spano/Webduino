package com.webduino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardSummaryOff extends WizardStep {

    /**
     * Tell WizarDroid that these are context variables.
     * These values will be automatically bound to any field annotated with {@link ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */
    @ContextVariable
    private boolean heaterAlwaysOn;
    @ContextVariable
    private boolean heaterOn30Minutes;
    @ContextVariable
    private boolean heaterOnToDate;

    @ContextVariable
    private double temperature;
    @ContextVariable
    private int sensorId;


    //You must have an empty constructor for every step
    public HeaterWizardSummaryOff() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.heaterwizard_summary_off, container, false);


        return v;
    }
}