package com.webduino;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;
import org.codepond.wizardroid.persistence.ContextVariable;

import com.google.gson.Gson;

import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.webduino.HeaterActuator.Command_Manual_On;
import static com.webduino.HeaterActuator.Command_Manual_Off;
import static com.webduino.fragment.SensorsFragment.HEATERWIZARD_REQUEST;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizard extends BasicWizardLayout {

    /**
     * Tell WizarDroid that these are context variables and set default values.
     * These values will be automatically bound to any field annotated with {@link ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */

    private int actuatorId = 0;
    String command = "";

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

    public HeaterWizard() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HeaterWizardActivity activity = (HeaterWizardActivity) getActivity();
        //actuatorId = activity.getActuatorId();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public WizardFlow onSetup() {

        HeaterWizardActivity a = (HeaterWizardActivity) getActivity();
        command = a.getCommand();
        actuatorId = a.getActuatorId();

        if (command.equals(Command_Manual_On)) {
            return new WizardFlow.Builder()
                    .addStep(HeaterWizardStep1.class)
                    .addStep(HeaterWizardStep2.class/*, true*/)
                    .addStep(HeaterWizardStep3.class)
                    .create();
        } else if (command.equals(Command_Manual_Off)) {
            return new WizardFlow.Builder()
                    .addStep(HeaterWizardStep1.class)
                    //.addStep(HeaterWizardStep2.class/*, true*/)
                    .addStep(HeaterWizardStep3.class)
                    .create();
        }
        return null;
    }

    /*
        You'd normally override onWizardComplete to access the wizard context and/or close the wizard
     */
    @Override
    public void onWizardComplete() {
        super.onWizardComplete();   //Make sure to first call the super method before anything else
        //... Access context variables here before terminating the wizard
        //...
        //String fullname = firstname + lastname;

        //HeaterWizardActivity a = (HeaterWizardActivity) getActivity();
        //actuatorId = a.getActuatorId();
        //new requestDataTask(requestDataCallback(), requestDataTask.POST_ACTUATOR_COMMAND).execute(actuatorId, Command_Manual_Start, 3000, temperature, sensorId, true);

        if (command.equals(Command_Manual_On)) {
            new requestDataTask(requestDataCallback(), requestDataTask.POST_ACTUATOR_COMMAND).execute(actuatorId, "start", 3000, temperature, sensorId, true);
        } else if (command.equals(Command_Manual_Off)) {
            new requestDataTask(requestDataCallback(), requestDataTask.POST_ACTUATOR_COMMAND).execute(actuatorId, "stop", 3000, 0.1, 0, false);
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

                if (!error && actuator != null) {
                    HeaterActuator heater = (HeaterActuator) actuator;

                    Gson gson = new Gson();
                    String strJson = gson.toJson(heater);

                    Intent intentMessage=new Intent();
                    intentMessage.putExtra("actuator",strJson);
                    //getActivity().setResult(HEATERWIZARD_REQUEST,intentMessage);

                    getActivity().setResult(RESULT_OK,intentMessage);
                    getActivity().finish();     //Terminate the wizard
                } else {
                    getActivity().setResult(RESULT_CANCELED);
                    getActivity().finish();     //Terminate the wizard
                }
            }
        };
    }


}