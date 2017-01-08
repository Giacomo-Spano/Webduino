package com.webduino.wizard;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.webduino.AsyncRequestDataResponse;
import com.webduino.elements.Actuator;
import com.webduino.elements.Program;
import com.webduino.elements.Programs;
import com.webduino.elements.Sensor;
import com.webduino.elements.requestDataTask;

import java.util.List;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class ProgramWizardActivity extends WizardActivity {

    private ProgramWizardFragment_Name nameStep;
    private ProgramWizardFragment_StepDate dateStep;
    private ProgramWizardFragment_StepTimeRange timeRangesStep;
    private ProgramWizardFragment_Days daysStep;

    private int programId = 0;
    private String command = "";
    private Program program;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        programId = bundle.getInt("programid");
        program = Programs.getFromId(programId);
        if (program == null)
            program = new Program();

        nameStep = new ProgramWizardFragment_Name();
        nameStep.init(program.name);
        addStep(nameStep);
        dateStep = new ProgramWizardFragment_StepDate();
        dateStep.init(program);
        addStep(dateStep);
        daysStep = new ProgramWizardFragment_Days();
        daysStep.init(program);
        addStep(daysStep);
        timeRangesStep = new ProgramWizardFragment_StepTimeRange();
        timeRangesStep.init(program.timeRanges);
        addStep(timeRangesStep);

        showFirstFragment();
    }

    public int getProgramId() {
        return programId;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public void onWizardComplete() {

        program.name = nameStep.getName();

        program.active = dateStep.getEnabled();
        program.dateEnabled = dateStep.getDateEnabled();
        program.startDate = dateStep.getStartDate();
        program.endDate = dateStep.getEndDate();
        program.startTime = dateStep.getStartTime();
        program.endTime = dateStep.getEndTime();

        program.Sunday = daysStep.getSunday();
        program.Monday = daysStep.getMonday();
        program.Tuesday = daysStep.getTuesday();
        program.Wednesday = daysStep.getWednesday();
        program.Thursday = daysStep.getThursday();
        program.Friday = daysStep.getFriday();
        program.Saturday = daysStep.getSaturday();

        program.timeRanges = timeRangesStep.getTimeRanges();

        new requestDataTask(this, requestDataCallback(), requestDataTask.POST_PROGRAM).execute(program);
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


            }

            @Override
            public void processFinishPrograms(List<Program> programs, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishPostProgram(boolean response, int requestType, boolean error, String errorMessage) {
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