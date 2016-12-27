package com.webduino;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardActivity extends FragmentActivity {

    private int actuatorId = 0;
    private String command = "";

    public static Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        Bundle bundle = getIntent().getExtras();
        actuatorId = bundle.getInt("actuatorid");
        command = bundle.getString("command");

        setContentView(R.layout.activity_heaterwizard);



    }

    public int getActuatorId() {
        return actuatorId;
    }

    public String getCommand() {
        return command;
    }
}