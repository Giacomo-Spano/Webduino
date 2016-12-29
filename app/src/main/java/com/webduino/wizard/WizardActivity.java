package com.webduino.wizard;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.webduino.R;
import com.webduino.wizard.HeaterWizardFragment_Step1;
import com.webduino.wizard.HeaterWizardFragment_Step2;
import com.webduino.wizard.HeaterWizardFragment_Summary;

import static com.webduino.HeaterActuator.Command_Manual_Off;
import static com.webduino.HeaterActuator.Command_Manual_On;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class WizardActivity extends AppCompatActivity {

    //private int actuatorId = 0;
    //private String command = "";

    public static Activity activity;

    Button nextButton, backButton;
    int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        setContentView(R.layout.heaterwizard_activity);

        backButton = (Button) findViewById(R.id.cancelButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFragment(step1);
                back();
            }
        });

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
                //showFragment(step2);
            }
        });


        //showFragment(step1);

    }

    protected void showFragment(Fragment fragment) {

        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }


    public void next() {

    }

    public void back() {

    }

    public Fragment getFragmentbyPosition() {
        return null;
    }

    public void onWizardComplete() {

    }

}