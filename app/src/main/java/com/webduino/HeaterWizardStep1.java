package com.webduino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardStep1 extends WizardStep {

    /**
     * Tell WizarDroid that these are context variables.
     * These values will be automatically bound to any field annotated with {@link ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */

    @ContextVariable
    private boolean heaterAlwaysOn = true;
    @ContextVariable
    private boolean heaterOn30Minutes = false;
    @ContextVariable
    private boolean heaterOnToDate = false;

    private RadioButton heaterAlwaysOnRadioButton;
    private RadioButton heaterOn30MinutesRadioButton;
    private RadioButton heaterOnToDateRadioButton;

    //You must have an empty constructor for every step
    public HeaterWizardStep1() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.heaterwizardstep1, container, false);

        heaterAlwaysOnRadioButton = (RadioButton) v.findViewById(R.id.alwaysRadioButton);
        heaterOn30MinutesRadioButton = (RadioButton) v.findViewById(R.id.for30MinutesRadioButton);
        heaterOnToDateRadioButton = (RadioButton) v.findViewById(R.id.toTimeRadioButton);

        heaterAlwaysOnRadioButton.setChecked(heaterAlwaysOn);
        heaterOn30MinutesRadioButton.setChecked(heaterOn30Minutes);
        heaterOnToDateRadioButton.setChecked(heaterOnToDate);
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
        heaterAlwaysOn = heaterAlwaysOnRadioButton.isChecked();
        heaterOn30Minutes = heaterOn30MinutesRadioButton.isChecked();
        heaterOnToDate = heaterOnToDateRadioButton.isChecked();

    }
}