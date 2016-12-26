package com.webduino;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardActivity extends FragmentActivity {

    private int actuatorId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heaterwizard);

        actuatorId = getIntent().getIntExtra("actuatorid",0);

        //HeaterWizard fragment = (HeaterWizard) getSupportFragmentManager().findFragmentById(R.actuatorId.tutorial_wizard_fragment);


    }

    public int getActuatorId() {
        return actuatorId;
    }
}