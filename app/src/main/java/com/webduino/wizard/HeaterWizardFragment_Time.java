package com.webduino.wizard;

//import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.webduino.R;
import com.webduino.fragment.DatePickerFragment;
import com.webduino.fragment.TimePickerFragment;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterWizardFragment_Time extends WizardFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    protected static int always = 1;
    protected static int forDuration = 2;
    protected static int toTime = 3;
    protected static int toNextProgram = 4;

    private int duration = 30;
    private Date toTimeDate;

    private RadioButton alwaysRadioButton, forDurationRadioButton, toTimeRadioButton, toNextProgramRadioButton;
    private EditText durationEditText, toTimeEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.wizard_fragment_heater_time, container, false);

        RadioGroup radiogroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radiogroup.setOnCheckedChangeListener(this);

        alwaysRadioButton = (RadioButton) v.findViewById(R.id.alwaysRadioButton);
        forDurationRadioButton = (RadioButton) v.findViewById(R.id.forDurationRadioButton);
        toTimeRadioButton = (RadioButton) v.findViewById(R.id.toTimeRadioButton);
        toNextProgramRadioButton = (RadioButton) v.findViewById(R.id.toNextProgramRadioButton);

        durationEditText = (EditText) v.findViewById(R.id.durationTimeEditText);
        durationEditText.setOnClickListener(this);
        durationEditText.setEnabled(true);
        duration = 30;
        durationEditText.setText("00:30");

        toTimeEditText = (EditText) v.findViewById(R.id.toTimeEditText);
        toTimeEditText.setOnClickListener(this);
        toTimeEditText.setEnabled(false);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 24);
        toTimeDate = cal.getTime();
        toTimeEditText.setText(df.format(toTimeDate.getTime()));

        forDurationRadioButton.setChecked(true);

        WizardActivity activity = (WizardActivity) getActivity();
        listener = activity;

        update();

        return v;
    }

    private void update() {
        alwaysRadioButton.setEnabled(false);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.alwaysRadioButton:
                durationEditText.setEnabled(false);
                toTimeEditText.setEnabled(false);
                break;
            case R.id.forDurationRadioButton:
                duration = 30;
                durationEditText.setEnabled(true);
                toTimeEditText.setEnabled(false);
                break;
            case R.id.toTimeRadioButton:
                duration = 30;
                durationEditText.setEnabled(false);
                toTimeEditText.setEnabled(true);
                break;
            case R.id.toNextProgramRadioButton:
                duration = 30;
                durationEditText.setEnabled(false);
                toTimeEditText.setEnabled(false);
                break;
        }
    }

    public int getDuration() {

        if (forDurationRadioButton.isChecked()) {

            Time time = Time.valueOf(durationEditText.getText()+":00");
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            duration = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);

        } else if (toTimeRadioButton.isChecked()) {

            Calendar current = Calendar.getInstance();
            Date currentDate = current.getTime();

            long timeDifference = toTimeDate.getTime() - currentDate.getTime();
            duration = (int) timeDifference /1000 / 60;
            if (duration < 0) duration = 0;

        }

        return duration;
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle != null) {
                int hours = bundle.getInt("hour");
                int minutes = bundle.getInt("minute");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hours);
                cal.set(Calendar.MINUTE, minutes);
                SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                String tag = bundle.getString("tag");
                if (tag.equals("duration"))
                    durationEditText.setText(df.format(cal.getTime()));
                /*else if (tag.equals("endtime")) {
                    toTimeDate.set(Calendar.HOUR_OF_DAY, hours);
                    toTimeDate.set(Calendar.MINUTE, minutes);
                    toTimeEditText.setText(df.format(cal.getTime()));
                }*/
            }
        }
    };



    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.durationTimeEditText){

            showTimePickerDialog(duration / 60, duration % 60,"Durata", "Impostare l'ora di fine", "duration", handler);

        } else if(view.getId() == R.id.toTimeEditText){

            //showTimePickerDialog(duration / 60, duration % 60,"Durata", "Impostare l'ora di fine", "endtime", handler);
            showDateTimePickerDialog(toTimeDate);
        }
    }

    public void showTimePickerDialog(int mHour, int mMinute, String title, String message, String tag, Handler mHandler) {

        Bundle b = new Bundle();
        b.putInt("hour", mHour);
        b.putInt("minute", mMinute);
        b.putString("message", message);
        b.putString("title", title);
        b.putString("tag", tag);

        TimePickerFragment timePicker = new TimePickerFragment(
                mHandler);
        timePicker.setArguments(b);
        FragmentManager fm = WizardActivity.activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(timePicker, message);
        ft.commit();
    }

    public void showDateTimePickerDialog(Date datetime) {
        DatePickerFragment dateFragment= new DatePickerFragment();
        dateFragment.setDate(datetime);
        dateFragment.setListener(new DatePickerFragment.DataPickerListener() {
                @Override
                public void setDate(Date date) {
                    toTimeDate.setTime(date.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    toTimeEditText.setText(df.format(date));
                }

                @Override
                public void cancel() {

                }
            });
        dateFragment.show(getFragmentManager(), "Date Picker");
    }
}
