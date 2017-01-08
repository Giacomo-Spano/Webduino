package com.webduino.wizard;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.webduino.WebduinoResponse;
import com.webduino.elements.Actuator;
import com.webduino.elements.requestDataTask;

import java.util.List;

import static com.webduino.elements.HeaterActuator.Command_Manual_Auto;
import static com.webduino.elements.HeaterActuator.Command_Manual_End;
import static com.webduino.elements.HeaterActuator.Command_Manual_Off;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardActivity extends WizardActivity {

    HeaterWizardFragment_Time timeStep;
    HeaterWizardFragment_Temperature temperatureStep;
    HeaterWizardFragment_Summary summaryStep;

    private int actuatorId = 0;
    private String command = "";

    private double temperature;
    private int sensorId;
    private int duration;
    private boolean remoteSensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        actuatorId = bundle.getInt("actuatorid");
        command = bundle.getString("command");

        if (command.equals(Command_Manual_Auto)) {

            timeStep = new HeaterWizardFragment_Time();
            addStep(timeStep);
            temperatureStep = new HeaterWizardFragment_Temperature();
            addStep(temperatureStep);

        } else if (command.equals(Command_Manual_Off)) {

            timeStep = new HeaterWizardFragment_Time();
            addStep(timeStep);

        } else if (command.equals(Command_Manual_End)) {

            summaryStep = new HeaterWizardFragment_Summary();
            summaryStep.setTitle("Conferma ripristino programma automatico");
            addStep(summaryStep);
        }
        showFirstFragment();
    }

    @Override
    public void onWizardComplete() {

        if (command.equals(Command_Manual_Auto)) {

            temperature = temperatureStep.getTemperature();
            sensorId = temperatureStep.getSensorId();
            duration = timeStep.getDuration();
            remoteSensor = temperatureStep.getRemoteSensor();

        } else if (command.equals(Command_Manual_Off)) {

            temperature = 0;
            sensorId = 0;
            duration = timeStep.getDuration();
            remoteSensor = false;

        } else if (command.equals(Command_Manual_End)) {

            temperature = 0;
            sensorId = 0;
            duration = 0;
            remoteSensor = false;
        }

        new requestDataTask(this, requestDataCallback(), requestDataTask.POST_ACTUATOR_COMMAND).execute(actuatorId, command, duration, temperature, sensorId, remoteSensor);
    }

    @Override
    public void OnNext(int option) {
        next();
    }

    @NonNull
    private WebduinoResponse requestDataCallback() {
        return new WebduinoResponse() {

            @Override
            public void processFinishSendCommand(Actuator actuator, boolean error, String errorMessage) {

                if (!error /*&& actuator != null*/) {

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