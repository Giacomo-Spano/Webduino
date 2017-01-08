package com.webduino.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Giacomo Spanò on 03/01/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DataPickerListener listener;

    public void setListener(DataPickerListener l) {
        listener = l;
    }

    public void setDate(Date date) {
        cal.setTime(date);
    }

    final Calendar cal = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),
                this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                Date date = cal.getTime();
                listener.setDate(date);
            }

        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        listener.cancel();
    }

    public interface DataPickerListener {
        void setDate(Date date);

        void cancel();
    }
}