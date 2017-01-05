package com.webduino.wizard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.webduino.elements.Actuator;
import com.webduino.AsyncRequestDataResponse;
import com.webduino.elements.Program;
import com.webduino.elements.Sensor;
import com.webduino.elements.requestDataTask;

import java.util.List;

import static com.webduino.elements.HeaterActuator.Command_Manual_Off;
import static com.webduino.elements.HeaterActuator.Command_Manual_Auto;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardActivity extends WizardActivity {

    HeaterWizardFragment_Step1 step1;
    HeaterWizardFragment_Step2 step2;
    HeaterWizardFragment_Summary summary;

    private int actuatorId = 0;
    private String command = "";


    private boolean heaterAlwaysOn;
    private boolean heaterOn30Minutes;
    private boolean heaterOnToDate;
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

        step1 = new HeaterWizardFragment_Step1();
        addStep(step1);
        step2 = new HeaterWizardFragment_Step2();
        addStep(step2);
        summary = new HeaterWizardFragment_Summary();
        addStep(summary);

        showFirstFragment();
    }

    public int getActuatorId() {
        return actuatorId;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public void onWizardComplete() {

        temperature = step2.getTemperature();
        sensorId = step2.getSensorId();
        duration = step1.getDuration();
        remoteSensor = step2.getRemoteSensor();

        new requestDataTask(this, requestDataCallback(), requestDataTask.POST_ACTUATOR_COMMAND).execute(actuatorId, command, duration, temperature, sensorId, remoteSensor);
    }

    @Override
    public void OnNext(int option) {
        next();
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

                    setResult(RESULT_OK);
                    finish();     //Terminate the wizard
                } else {
                    setResult(RESULT_CANCELED);
                    finish();     //Terminate the wizard
                }
            }

            @Override
            public void processFinishPrograms(List<Program> programs, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishPostProgram(boolean response, boolean error, String errorMessage) {

            }
        };
    }
}