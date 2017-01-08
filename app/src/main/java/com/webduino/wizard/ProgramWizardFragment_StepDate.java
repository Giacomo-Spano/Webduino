package com.webduino.wizard;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.webduino.fragment.DatePickerFragment;
import com.webduino.R;
import com.webduino.elements.Program;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class ProgramWizardFragment_StepDate extends WizardFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private boolean enabled;
    public boolean dateEnabled;
    public String name;
    protected Date startDateTime = new Date();
    protected Date endDateTime = new Date();
    private RadioButton alwaysCheckBox, neverCheckBox, dateRangeCheckBox;
    private EditText startDateEdit, endDateEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.wizard_fragment_program_date, container, false);

        RadioGroup radiogroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radiogroup.setOnCheckedChangeListener(this);

        WizardActivity activity = (WizardActivity) getActivity();
        listener = activity;

        startDateEdit = (EditText) v.findViewById(R.id.durationTimeEditText);
        startDateEdit.setOnClickListener(this);
        endDateEdit = (EditText) v.findViewById(R.id.endDateEditText);
        endDateEdit.setOnClickListener(this);

        alwaysCheckBox = (RadioButton) v.findViewById(R.id.alwaysRadioButton);
        neverCheckBox = (RadioButton) v.findViewById(R.id.neverRadioButton);
        dateRangeCheckBox = (RadioButton) v.findViewById(R.id.dateRangeRadioButton);

        update();
        return v;
    }

    public void init(Program program) {

        startDateTime = getDateTimeFromDateAndTime(program.startDate,program.startTime);
        endDateTime = getDateTimeFromDateAndTime(program.endDate,program.endTime);
        dateEnabled = program.dateEnabled;
        enabled = program.active;
    }

    protected Date getDateTimeFromDateAndTime(Date date, Date time) {
        Calendar cdate = Calendar.getInstance();
        Calendar ctime = Calendar.getInstance();
        if (date != null && time != null) {
            cdate.setTime(date);
            ctime.setTime(time);
        }
        cdate.set(Calendar.HOUR,ctime.get(Calendar.HOUR));
        cdate.set(Calendar.MINUTE,ctime.get(Calendar.MINUTE));
        return cdate.getTime();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.alwaysRadioButton:
                enabled = true;
                dateEnabled = false;
                startDateEdit.setEnabled(false);
                endDateEdit.setEnabled(false);
                break;
            case R.id.dateRangeRadioButton:
                enabled = true;
                dateEnabled = true;
                startDateEdit.setEnabled(true);
                endDateEdit.setEnabled(true);
                break;
            case R.id.neverRadioButton:
                enabled = false;
                startDateEdit.setEnabled(false);
                endDateEdit.setEnabled(false);
                break;
        }
    }

    public void update() {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        startDateEdit.setText(df.format(startDateTime));
        endDateEdit.setText(df.format(endDateTime));

        if (enabled && !dateEnabled) {
            alwaysCheckBox.setChecked(true);
            startDateEdit.setEnabled(false);
            endDateEdit.setEnabled(false);

        } else if (enabled && dateEnabled) {
            dateRangeCheckBox.setChecked(true);
            startDateEdit.setEnabled(true);
            endDateEdit.setEnabled(true);

        } else {
            neverCheckBox.setChecked(true);
            startDateEdit.setEnabled(false);
            endDateEdit.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {

        DatePickerFragment dateFragment= new DatePickerFragment();
        if (v.getId() == R.id.durationTimeEditText){
            dateFragment.setDate(startDateTime);
            dateFragment.setListener(new DatePickerFragment.DataPickerListener() {
                @Override
                public void setDate(Date date) {
                    startDateTime = date;
                    update();
                }

                @Override
                public void cancel() {

                }
            });
            dateFragment.show(getFragmentManager(), "Date Picker");

        } else if (v.getId() == R.id.endDateEditText) {
            dateFragment.setDate(endDateTime);
            dateFragment.setListener(new DatePickerFragment.DataPickerListener() {
                @Override
                public void setDate(Date date) {
                    endDateTime = date;
                    update();
                }

                @Override
                public void cancel() {

                }
            });
            dateFragment.show(getFragmentManager(), "Date Picker");
        }
    }

    public boolean getDateEnabled() {

        return dateEnabled;
    }

    public Date getStartDate() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateTime);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        return cal.getTime();
    }

    public Date getEndDate() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(endDateTime);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        return cal.getTime();
    }

    public Time getStartTime() {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Time time = Time.valueOf(df.format(startDateTime)+":00");
        return time;
    }

    public Time getEndTime() {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Time time = Time.valueOf(df.format(endDateTime)+":00");
        return time;
    }

    public boolean getEnabled() {
        return enabled;
    }
}
