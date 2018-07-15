package com.webduino.wizard;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.webduino.AsyncRequestDataResponse;
import com.webduino.elements.Actuators;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.requestDataTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

//import static com.webduino.elements.HeaterActuator.Command_Auto;
import static com.webduino.elements.HeaterActuator.Command_Manual;
import static com.webduino.elements.HeaterActuator.Command_Off;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class HeaterWizardActivity extends WizardActivity implements AsyncRequestDataResponse {

    HeaterWizardFragment_Time timeStep;
    HeaterWizardFragment_Temperature temperatureStep;
    HeaterWizardFragment_Summary summaryStep;

    private int actuatorId = 0;
    private int shieldid = 0;
    private String command = "";

    private double temperature;
    private int zoneId;
    private int duration;
    private boolean remoteSensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        actuatorId = bundle.getInt("actuatorid");
        shieldid = bundle.getInt("shieldid");
        command = bundle.getString("command");

        HeaterActuator heater = (HeaterActuator) Actuators.getFromId(actuatorId);
        if (heater != null) {
            /*if (heater.getLocalsensor()) {
                sensorId = 0;
                remoteSensor = false;
            } else {
                remoteSensor = true;
                sensorId = heater.getSensorId();
            }*/
            zoneId = heater.getZoneId();
            temperature = 22;
        }

        if (command.equals(Command_Manual)) {

            timeStep = new HeaterWizardFragment_Time();
            addStep(timeStep);
            temperatureStep = new HeaterWizardFragment_Temperature();
            temperatureStep.init(temperature, zoneId);
            addStep(temperatureStep);

        } /*else if (command.equals(Command_Manual_Off)) {

            timeStep = new HeaterWizardFragment_Time();
            addStep(timeStep);

        } */ else if (command.equals(Command_Off)) {

            summaryStep = new HeaterWizardFragment_Summary();
            summaryStep.setTitle("Conferma ripristino programma automatico");
            addStep(summaryStep);
        }
        showFirstFragment();
    }

    @Override
    public void onWizardComplete() {

        String endtime = "";//new Date();//null;//new LocalTime();
        boolean nexttimerange = false;
        if (command.equals(Command_Manual)) {

            temperature = temperatureStep.getTemperature();
            zoneId = temperatureStep.getZoneId();
            duration = timeStep.getDuration();
            endtime = null;
            nexttimerange = false;

        } else if (command.equals(Command_Off)) {

            temperature = 0;
            zoneId = 0;
            duration = 0;
            endtime = null;
            nexttimerange = false;
        }
        JSONObject json = new JSONObject();


        try {
            json.put("shieldid", "" + shieldid);
            json.put("actuatorid", "" + actuatorId);

            json.put("command", command);
            if (nexttimerange) {
                json.put("nexttimerange", nexttimerange);
            } else if (endtime != null && !endtime.equals("")) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                json.put("endtime", df.format(endtime));
            } else {
                json.put("duration", "" + duration);
            }
            //json.put("target", "" + target);
            json.put("zone", "" + zoneId);

            new requestDataTask(this, this, requestDataTask.POST_SHIELD_COMMAND).execute(json/*shieldid, actuatorId, command, duration, endtime, nexttimerange, temperature, zoneId*/);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnNext(int option) {
        next();
    }

    @Override
    public void processFinish(Object result, int requestType, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishRegister(long shieldId, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishSendCommand(String response, boolean error, String errorMessage) {


        if (!error) {

            Context context = getApplicationContext();
            CharSequence text = "Hello toast!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Comando inviato", duration);
            toast.show();

            setResult(RESULT_OK);
            finish();     //Terminate the wizard
        } else {
            setResult(RESULT_CANCELED);
            finish();     //Terminate the wizard
        }
    }

    @Override
    public void processFinishPostProgram(boolean response, int requestType, boolean error, String errorMessage) {

    }

    @Override
    public void processFinishObjectList(List<Object> list, int requestType, boolean error, String errorMessage) {

    }
}