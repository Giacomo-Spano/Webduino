package com.webduino.wizard;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.webduino.Actuator;
import com.webduino.AsyncRequestDataResponse;
import com.webduino.HeaterActuator;
import com.webduino.Sensor;
import com.webduino.requestDataTask;
import com.webduino.wizard.HeaterWizardFragment_Step1;
import com.webduino.wizard.HeaterWizardFragment_Step2;
import com.webduino.wizard.HeaterWizardFragment_Summary;
import com.webduino.wizard.WizardActivity;

import java.util.List;

import static com.webduino.HeaterActuator.Command_Manual_Off;
import static com.webduino.HeaterActuator.Command_Manual_On;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardActivity extends WizardActivity {

    private int actuatorId = 0;
    private String command = "";

    //public static Activity activity;

    HeaterWizardFragment_Step1 step1;
    HeaterWizardFragment_Step2 step2;
    HeaterWizardFragment_Summary summary;

    private boolean heaterAlwaysOn;
    private boolean heaterOn30Minutes;
    private boolean heaterOnToDate;
    private double temperature;
    private int sensorId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity = this;

        Bundle bundle = getIntent().getExtras();
        actuatorId = bundle.getInt("actuatorid");
        command = bundle.getString("command");

        step1 = new HeaterWizardFragment_Step1();
        step2 = new HeaterWizardFragment_Step2();
        summary = new HeaterWizardFragment_Summary();

        showFragment(getFragmentbyPosition());

    }

    @Override
    public void back() {

        if (position > 0)
            position--;
        showFragment(getFragmentbyPosition());

    }

    @Override
    public void next() {

        if (position == 2) {
            onWizardComplete();
        } else {
            if (position < 2)
                position++;
            showFragment(getFragmentbyPosition());
        }
    }

    public int getActuatorId() {
        return actuatorId;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public Fragment getFragmentbyPosition() {
        switch (position) {
            case 0:
                backButton.setEnabled(false);
                nextButton.setText("Indietro");
                nextButton.setText("Avanti");
                return step1;
            case 1:
                backButton.setEnabled(true);
                nextButton.setText("Indietro");
                nextButton.setText("Avanti");
                return step2;
            case 2:
                backButton.setEnabled(true);
                nextButton.setText("Indietro");
                nextButton.setText("Invia");
                return summary;
        }
        return null;
    }

    @Override
    public void onWizardComplete() {

        temperature = step2.getTemperature();
        sensorId = step2.getSensorId();

        if (command.equals(Command_Manual_On)) {
            new requestDataTask(this, requestDataCallback(), requestDataTask.POST_ACTUATOR_COMMAND).execute(actuatorId, "start", 1, temperature, sensorId, true);
        } else if (command.equals(Command_Manual_Off)) {
            new requestDataTask(this, requestDataCallback(), requestDataTask.POST_ACTUATOR_COMMAND).execute(actuatorId, "stop", 1, 0.1, 0, false);
        }
    }

    @NonNull
    private AsyncRequestDataResponse requestDataCallback() {
        return new AsyncRequestDataResponse() {

            @Override
            public void processFinishRegister(long shieldId, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishSensors(List<Sensor> sensors, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishActuators(List<Actuator> actuators, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishSendCommand(Actuator actuator, boolean error, String errorMessage) {

                if (!error /*&& actuator != null*/) {

                    /*HeaterActuator heater = (HeaterActuator) actuator;

                    Gson gson = new Gson();
                    String strJson = gson.toJson(heater);

                    Intent intentMessage=new Intent();
                    intentMessage.putExtra("actuator",strJson);

                    setResult(RESULT_OK,intentMessage);*/
                    setResult(RESULT_OK);
                    finish();     //Terminate the wizard
                } else {
                    setResult(RESULT_CANCELED);
                    finish();     //Terminate the wizard
                }
            }
        };
    }
}